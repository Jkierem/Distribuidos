package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import shared.logic.AbstractConnection;
import shared.model.Operation;
import shared.model.Proposal;
import shared.model.Result;
import shared.model.Vote;

public class SourceConnection extends AbstractConnection<String> {
	
	private Vector<Proposal> activeProposals;
	
	public SourceConnection(Socket socket, Vector<Proposal> activeProposals) {
		super(socket);
		this.activeProposals = activeProposals;
	}
	
	public void sendProposalList( Result res , DataOutputStream out ) {
		String proposals = "" ;
		for(int i = 0 ;  i < this.activeProposals.size() ; i++ ) {
			String pre = "";
			if( proposals.compareTo("") != 0 ) {
				pre = ",";
			}
			proposals += pre + this.activeProposals.get(i).getName();
		}
		proposals = res.toString() + ":" + proposals;
		System.out.println(proposals);
		try {
			out.writeUTF(proposals);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Result processVote(String data) {
		Vote v = Vote.fromCSVString(data);
		boolean success = false;
		for( Proposal p : this.activeProposals ) {
			if( p.getName().compareTo(v.getTopic()) == 0 ){
				success = p.receiveVote(v);
			}
		}
		return success ? Result.SUCCESS : Result.FAILURE;
	}
	
	public void reportVotes() {
		String props = "Proposals: \n";
		for( Proposal p : this.activeProposals ) {
			props += p.toString() + "\n";
		}
		System.out.println(props);
	}
	
	@Override
	public void onReceive(String msg, DataOutputStream out) {
		String[] tokens = msg.split(":");
		Operation op = Operation.valueOf(tokens[0]);
		Result res = Result.SUCCESS;
		if( op == Operation.VOTE ) {
			res = this.processVote(tokens[1]);
			this.reportVotes();
		}
		this.sendProposalList(res,out);
		super.close();
	}

	@Override
	public String listen(DataInputStream in, DataOutputStream out) throws IOException {
		return in.readUTF();
	}

}
