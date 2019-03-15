package dns.services;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import dns.connection.RegisterConnection;
import shared.model.ServerReference;

public class DNSRegister<Container extends BlockingQueue<ServerReference> > extends AbstractDNSService<RegisterConnection<Container>,Container> {
	
	public DNSRegister(String name, int port, Container proxyList) {
		super(name, port, proxyList);
	}
	
	public DNSRegister(String name, int port, Container proxyList, boolean verbose) {
		super(name, port, proxyList, verbose);
	}

	@Override
	public RegisterConnection<Container> createConnection(Socket socket, Container proxyRefs) {
		return new RegisterConnection<Container>( socket, proxyRefs );
	}
}
