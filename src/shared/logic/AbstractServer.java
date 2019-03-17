package shared.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import shared.utils.ConditionalLogger;

public abstract class AbstractServer extends Thread {
	
	private int port;
	private volatile boolean open;
	protected ConditionalLogger logger;
	
	public AbstractServer( String name , int port) {
		this.port = port;
		this.open = true;
		this.logger = new ConditionalLogger(true);
	}
	
	public AbstractServer( String name , int port , boolean verbose ) {
		this.port = port;
		this.open = true;
		this.logger = new ConditionalLogger(verbose, name + ": ");
	}
	
	@Override
	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(this.port);
			while(this.open) {
				this.logger.log("Waiting for connections...");
				Socket socket = server.accept();
				this.logger.log("Got connection! About to communicate");
				this.onRequest(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( server != null ) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void close() {
		this.open = false;
	}
	
	public abstract void onRequest( Socket socket );
	
}
