package shared.logic;

import java.io.IOException;
import java.net.Socket;

import shared.model.ServerReference;

public abstract class AbstractClient extends Thread{
	
	private ServerReference serverInfo;
	
	public AbstractClient( ServerReference serverInfo ) {
		this.serverInfo = serverInfo;
	}
	
	public AbstractClient( String host , int port ) {
		this.serverInfo = new ServerReference(host,port);
	}
	
	public static Socket attemptConnection( ServerReference server ) {
		Socket socket = null;
		try {
			socket = new Socket( server.getAddress() , server.getPort() );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}
	
	@Override
	public void run() {
		Socket socket = AbstractClient.attemptConnection(this.serverInfo);
		this.onConnect(socket);
	}
	
	public abstract void onConnect( Socket socket );
	
}
