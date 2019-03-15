package dns.services;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import dns.connection.ClientConnection;
import shared.model.ServerReference;

public class DNSService<Container extends BlockingQueue<ServerReference> > extends AbstractDNSService<ClientConnection<Container>,Container>{
	
	public DNSService( String name, int port, Container proxyList) {
		super( name, port, proxyList );
	}
	
	public DNSService( String name ,int port, Container proxyList, boolean verbose) {
		super( name, port, proxyList, verbose );
	}
	
	@Override
	public ClientConnection<Container> createConnection(Socket socket, Container proxyRefs) {
		return new ClientConnection<Container>(socket, proxyRefs);
	}
}
