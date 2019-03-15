package shared.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class AbstractConnection extends Thread {
	private Socket socket;
	private boolean open;
	
	public AbstractConnection( Socket socket ) {
		this.socket = socket;
		this.open = true;
	}
	
	@Override
	public void run() {
		DataInputStream in = null;
		DataOutputStream out = null;
		try {
			in = new DataInputStream( this.socket.getInputStream() );
			out = new DataOutputStream( this.socket.getOutputStream() );
			onConnect( in , out );
			afterConnect( in , out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( in != null ) {				
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void afterConnect( DataInputStream in , DataOutputStream out ) throws IOException {
		while(this.open) {
			String msg = in.readUTF();
			this.onReceive(msg, out);
		}
	}
	
	public synchronized void close() {
		this.open = false;
	}
	
	public void onReceive( String msg , DataOutputStream out ) {}
	public void onConnect( DataInputStream in , DataOutputStream out ) {}
}