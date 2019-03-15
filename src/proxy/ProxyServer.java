package proxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import shared.model.ServerReference;

public class ProxyServer extends Thread {
	
	private ServerReference info;
	private ServerReference dnsServer;
	
	public ProxyServer( ServerReference info, ServerReference dnsReference) {
		this.info = info;
		this.dnsServer = dnsReference;
	}
	
	@Override
	public void run() {
		Socket socket = null;
		try {
			socket = new Socket( dnsServer.getAddress() , dnsServer.getPort() );
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			System.out.println("About to send: "+ this.info.toCSVString());
			out.writeUTF(this.info.toCSVString());
			System.out.println("sent");
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
