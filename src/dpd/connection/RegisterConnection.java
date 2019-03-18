package dpd.connection;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import shared.logic.AbstractStringConnection;
import shared.model.ServerReference;

public class RegisterConnection<Container extends BlockingQueue<ServerReference>> extends AbstractStringConnection {
	
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
			e.printStackTrace();
		}
		super.close();
	}

}
