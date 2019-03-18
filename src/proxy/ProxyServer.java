package proxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import shared.model.ServerReference;
import shared.utils.ConditionalLogger;

public class ProxyServer extends Thread {
	
	private ServerReference info;
	private ServerReference dpdServer;
	private ConditionalLogger logger;
	private volatile ConcurrentHashMap<String, ServerReference> directory;
	private long cacheDuration;
	
	public ProxyServer( ServerReference info, ServerReference dnsReference) {
		this.info = info;
		this.dpdServer = dnsReference;
		this.logger = new ConditionalLogger(false);
		this.cacheDuration = 2;
	}
	
	public ProxyServer( ServerReference info, ServerReference dnsReference, boolean verbose) {
		this.info = info;
		this.dpdServer = dnsReference;
		this.logger = new ConditionalLogger(verbose);
		this.cacheDuration = 2;
	}
	
	public void getInfoSources(){
		//TODO : Implement this
		this.directory = new ConcurrentHashMap<String, ServerReference>();
	}
	
	public void maintainTopicCache() {
		Instant before = Instant.now();
		long ellapsed = 0;
		while(true) {
			Instant after = Instant.now();
			ellapsed = Duration.between(before, after).toMinutes();
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
