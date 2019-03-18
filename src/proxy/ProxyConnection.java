package proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import shared.logic.AbstractStringConnection;
import shared.model.Operation;
import shared.model.ServerReference;
import shared.model.Vote;

public class ProxyConnection extends AbstractStringConnection {

	private ConcurrentHashMap<String, ServerReference> directory;
	
	public ProxyConnection(Socket socket, ConcurrentHashMap<String, ServerReference> directory) {
		super(socket);
		this.directory = directory;
	}
	
	@Override
	public void onReceive(String msg, DataOutputStream out) {
		String[] tokens = msg.split(":");
		Operation op = Operation.valueOf(tokens[0]);
		String data = tokens.length > 1 ? tokens[1] : "";
		try {
			if( op == Operation.VOTE ) {
				this.onVote(Vote.fromCSVString(data), out);
			} else if (op == Operation.LIST ) {
				this.onList(out);
			} else if (op == Operation.PING ) {
				this.onPing(out);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onVote(Vote data , DataOutputStream out ) throws UnknownHostException, IOException {
		ServerReference server = this.directory.get(data.getTopic());
		Socket infoSocket = new Socket( server.getAddress() , server.getPort() );
		DataInputStream infoIn = new DataInputStream(infoSocket.getInputStream());
		DataOutputStream infoOut = new DataOutputStream(infoSocket.getOutputStream());
		infoOut.writeUTF(data.toVoteMessage());
		String response = infoIn.readUTF();
		infoIn.close();
		infoSocket.close();
		out.writeUTF(response);
		super.close();
	}
	
	public void onList( DataOutputStream out ) throws IOException {
		String topics = "";
		for( String topic : this.directory.keySet() ) {
			topics+=topic;
		}
		out.writeUTF(topics);
	}
	
	public void onPing( DataOutputStream out ) throws IOException {
		out.writeUTF("PONG");
		super.close();
	}

}
