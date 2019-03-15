package dns.connection;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import shared.logic.AbstractConnection;
import shared.model.ServerReference;

public class RegisterConnection<Container extends BlockingQueue<ServerReference>> extends AbstractConnection {
	
	private Container proxyList;
	
	public RegisterConnection( Socket s , Container proxyList ) {
		super(s);
		this.proxyList = proxyList;
	}

	@Override
	public void onReceive(String msg, DataOutputStream out) {
		System.out.println("Received: "+ msg);
		try {
			this.proxyList.put(ServerReference.fromCSVString(msg));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.close();
	}

}
