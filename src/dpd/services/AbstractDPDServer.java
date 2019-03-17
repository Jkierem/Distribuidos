package dpd.services;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import shared.logic.AbstractConnection;
import shared.logic.AbstractServer;
import shared.model.ServerReference;

public abstract class AbstractDPDServer<ConnectionType extends AbstractConnection, Container extends BlockingQueue<ServerReference>> extends AbstractServer{
	
	private Container proxyRefs;
	
	public AbstractDPDServer(String name, int port, Container proxyRefs, boolean verbose) {
		super(name, port, verbose);
		this.proxyRefs = proxyRefs;
	}

	public AbstractDPDServer(String name, int port, Container proxyRefs) {
		super(name, port);
		this.proxyRefs = proxyRefs;
	}

	@Override
	public void onRequest(Socket socket) {
		this.createConnection(socket, this.proxyRefs).start();
	}
	
	public abstract ConnectionType createConnection( Socket socket , Container proxyRefs );
	
}
