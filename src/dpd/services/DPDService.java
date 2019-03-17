package dpd.services;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import dpd.connection.ClientConnection;
import shared.model.ServerReference;

public class DPDService<Container extends BlockingQueue<ServerReference> > extends AbstractDPDServer<ClientConnection<Container>,Container>{
	
	public DPDService( String name, int port, Container proxyList) {
		super( name, port, proxyList );
	}
	
	public DPDService( String name ,int port, Container proxyList, boolean verbose) {
		super( name, port, proxyList, verbose );
	}
	
	@Override
	public ClientConnection<Container> createConnection(Socket socket, Container proxyRefs) {
		return new ClientConnection<Container>(socket, proxyRefs);
	}
}
