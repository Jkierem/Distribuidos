package shared.logic;

import java.io.IOException;
import java.net.Socket;

import shared.model.ServerReference;

public abstract class AbstractClient extends Thread{
	
	public AbstractClient( ) {
	
	}
	
	public void tryConnect( ServerReference server ) {
		Socket socket = null;
		try {
			socket = new Socket( server.getAddress() , server.getPort() );
			this.onConnect(socket , server );
		} catch (IOException e) {
			this.onFailed(server , e);
		}
	}
	
	@Override
	public void run() {
		this.onRun();
	}
	
	public abstract void onRun();
	public abstract void onConnect( Socket socket , ServerReference info );
	public abstract void onFailed( ServerReference server , IOException e );
}
