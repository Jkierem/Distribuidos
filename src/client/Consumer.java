package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import shared.logic.AbstractClient;
import shared.model.ServerReference;

public class Consumer extends AbstractClient {
	
	private boolean open;

	public Consumer(ServerReference serverInfo) {
		super(serverInfo);
		this.open = true;
	}

	public Consumer(String host, int port) {
		super(host, port);
		this.open = true;
	}
	
	public void close() {
		this.open = false;
	}

	@Override
	public void onConnect(Socket socket) {
		DataInputStream in;
		try {
			in = new DataInputStream(socket.getInputStream());
			String info = in.readUTF();
			ServerReference proxyInfo = ServerReference.fromCSVString(info);
			System.out.println(proxyInfo.toCSVString());
			Socket proxy = AbstractClient.attemptConnection(proxyInfo);
			in = new DataInputStream(proxy.getInputStream());
			while(this.open) {
				String msg = in.readUTF();
				System.out.println(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
