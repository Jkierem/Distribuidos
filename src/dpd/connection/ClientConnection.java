package dpd.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import shared.logic.AbstractStringConnection;
import shared.model.ServerReference;

public class ClientConnection<Container extends BlockingQueue<ServerReference>> extends AbstractStringConnection {
	
	private Container proxyList; 
	
	public ClientConnection(Socket socket, Container proxyList ) {
		super(socket);
		this.proxyList = proxyList;
	}
	
	@Override
	public void onConnect(DataInputStream in, DataOutputStream out) {
		ServerReference current = null;
		try {
			current = this.proxyList.take();
			System.out.println("About to send: "+ current.toCSVString());
			out.writeUTF(current.toCSVString());
			if( current != null ) {			
				System.out.println("About to put: "+ current.toCSVString());
				this.proxyList.put(current);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		super.close();
	}

}
