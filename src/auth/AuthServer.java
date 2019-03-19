package auth;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import shared.logic.AbstractServer;
import shared.utils.CSVReader;

public class AuthServer extends AbstractServer{
	
	private ConcurrentHashMap<String, String> userList;
	
	public AuthServer( String path, int port, boolean verbose) throws IOException{
		super("Auth", port, verbose);
		this.userList = this.readFile(path);
		this.report();
	}
	
	public AuthServer( String path, int port ) throws IOException {
		super("Auth", port, false);
		this.userList = this.readFile(path);
		this.report();
	}
	
	private ConcurrentHashMap<String, String> readFile(String path) throws IOException{
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
		List<String[]> users = new CSVReader(path).readFile();
		for( String[] tuple : users ) {
			map.put(tuple[0], tuple[1]);
		}
		return map;
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
