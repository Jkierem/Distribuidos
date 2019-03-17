package auth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import shared.logic.AbstractServer;

public class AuthServer extends AbstractServer{
	
	private ConcurrentHashMap<String, String> userList;
	
	public AuthServer( String path, int port, boolean verbose) throws IOException{
		super("Auth", port, verbose);
		FileHandler fh = this.createHandler(path);
		this.userList = fh.readUsers();
		this.report();
	}
	
	public AuthServer( String path, int port ) throws IOException {
		super("Auth", port, false);
		FileHandler fh = this.createHandler(path);
		this.userList = fh.readUsers();
		this.report();
	}
	
	private FileHandler createHandler( String path ) throws FileNotFoundException {
		FileHandler fh =  new FileHandler(path);
		return fh;
	}
	
	public void report() {
		String users = "";
		for( String key : this.userList.keySet() ) {
			users += "- " + key + ":=" + this.userList.get(key) + "\n";  
		}
		super.logger.log("Known Users:\n"+ users);
	}

	@Override
	public void onRequest(Socket socket) {
		new AuthConnection(socket, userList, super.logger).start();
	}
}
