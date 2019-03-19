package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import shared.logic.AbstractServer;
import shared.model.Proposal;
import shared.utils.CSVReader;

public class SourceServer extends AbstractServer {
	
	private Vector<Proposal> activeProposals;
	private LinkedBlockingQueue<Proposal> waitList;
	
	public SourceServer(String name, int port, String path) throws FileNotFoundException, IOException, InterruptedException {
		super(name, port);
		this.activeProposals = new Vector<Proposal>();
		this.waitList = new LinkedBlockingQueue<Proposal>();
		this.readProposals(path);
		new PetitionDiscoveryService(waitList, activeProposals).start();
	}

	public SourceServer(String name, int port, String path, boolean verbose) throws FileNotFoundException, IOException, InterruptedException {
		super(name, port, verbose);
		this.activeProposals = new Vector<Proposal>();
		this.waitList = new LinkedBlockingQueue<Proposal>();
		this.readProposals(path);
		new PetitionDiscoveryService(waitList, activeProposals).start();
	}
	
	private void readProposals( String path ) throws FileNotFoundException, IOException, InterruptedException {
		List<String[]> tuples = new CSVReader(path).readFile();
		for( String[] tuple : tuples ) {
			this.waitList.put(new Proposal(tuple[0]));
		}
		this.activeProposals.add(this.waitList.take());
	}

	@Override
	public void onRequest(Socket socket) {
		new SourceConnection(socket, this.activeProposals).start();
	}

}
