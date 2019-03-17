package proxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import shared.model.ServerReference;
import shared.utils.ConditionalLogger;

public class ProxyServer extends Thread {
	
	private ServerReference info;
	private ServerReference dnsServer;
	private ConditionalLogger logger;
	
	public ProxyServer( ServerReference info, ServerReference dnsReference) {
		this.info = info;
		this.dnsServer = dnsReference;
		this.logger = new ConditionalLogger(false);
	}
	
	public ProxyServer( ServerReference info, ServerReference dnsReference, boolean verbose) {
		this.info = info;
		this.dnsServer = dnsReference;
		this.logger = new ConditionalLogger(verbose);
	}
	
	@Override
	public void run() {
		Socket socket = null;
		try {
			socket = new Socket( dnsServer.getAddress() , dnsServer.getPort() );
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			this.logger.log("About to send: "+ this.info.toCSVString());
			out.writeUTF(this.info.toCSVString());
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if( socket != null ) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
