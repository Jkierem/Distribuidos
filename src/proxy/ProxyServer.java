package proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import shared.model.Operation;
import shared.model.ServerReference;
import shared.utils.ConditionalLogger;

public class ProxyServer extends Thread {
	
	private ServerReference info;
	private ServerReference dpdServer;
	private ConditionalLogger logger;
	private volatile ConcurrentHashMap<String, ServerReference> directory;
	private List<ServerReference> sources;
 	private long cacheDuration;
	
	public ProxyServer( ServerReference info, ServerReference dnsReference, List<ServerReference> sources) {
		this.info = info;
		this.dpdServer = dnsReference;
		this.logger = new ConditionalLogger(false);
		this.sources = sources;
		this.cacheDuration = 30000;
		this.directory = new ConcurrentHashMap<String, ServerReference>();
	}
	
	public ProxyServer( ServerReference info, ServerReference dnsReference , List<ServerReference> sources, boolean verbose) {
		this.info = info;
		this.dpdServer = dnsReference;
		this.sources = sources;
		this.logger = new ConditionalLogger(verbose, "ProxyServer: ");
		this.cacheDuration = 30000;
		this.directory = new ConcurrentHashMap<String, ServerReference>();
	}
	
	public void getInfoSources() throws UnknownHostException, IOException{
		this.logger.log("Filling cache...");
		for( ServerReference server : this.sources ) {
			Socket socket = new Socket( server.getAddress() , server.getPort() );
			DataInputStream in = new DataInputStream( socket.getInputStream() );
			DataOutputStream out = new DataOutputStream( socket.getOutputStream() );
			out.writeUTF(Operation.LIST.toString());
			String[] tokens = in.readUTF().split(":");
			String petitions = tokens.length > 1 ? tokens[1]: "";
			for( String p : petitions.split(",") ) {
				this.directory.put(p, server);
			}
			socket.close();
		}
		String petitions = "";
		for( String key : this.directory.keySet() ) {
			petitions += "- " + key + "\n";
		}
		this.logger.log("Active petitions: \n"+ petitions);
	}
	
	public void maintainTopicCache() throws UnknownHostException, IOException {
		Instant before = Instant.now();
		long ellapsed = 0;
		while(true) {
			Instant after = Instant.now();
			ellapsed = Duration.between(before, after).toMillis();
			if( ellapsed > cacheDuration ) {
				this.getInfoSources();
				before = after;
			}
		}
	}
	
	@Override
	public void run() {
		Socket socket = null;
		try {
			socket = new Socket( dpdServer.getAddress() , dpdServer.getPort() );
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			this.logger.log("About to send: "+ this.info.toCSVString());
			out.writeUTF(this.info.toCSVString());
			socket.close();
			this.getInfoSources();
			new ProxyService( "Proxy", this.info.getPort() , this.directory , true ).start();
			this.maintainTopicCache();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( socket != null ) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
