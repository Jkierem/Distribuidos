package shared.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import shared.utils.ConditionalLogger;

public abstract class AbstractServer extends Thread {
	private String name;
	private int port;
	private boolean open;
	private ConditionalLogger logger;
	
	public AbstractServer( String name , int port) {
		this.name = name;
		this.port = port;
		this.open = true;
		this.logger = new ConditionalLogger(true);
	}
	
	public AbstractServer( String name , int port , boolean verbose ) {
		this.name = name;
		this.port = port;
		this.open = true;
		this.logger = new ConditionalLogger(verbose);
	}
	
	
	@Override
	public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(this.port);
			while(this.open) {
				this.logger.log(name + ": Waiting for connections...");
				Socket socket = server.accept();
				this.logger.log(name + ": Got connection! About to communicate");
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
