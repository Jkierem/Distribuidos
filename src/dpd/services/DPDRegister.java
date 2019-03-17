package dpd.services;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import dpd.connection.RegisterConnection;
import shared.model.ServerReference;

public class DPDRegister<Container extends BlockingQueue<ServerReference> > extends AbstractDPDServer<RegisterConnection<Container>,Container> {
	
	public DPDRegister(String name, int port, Container proxyList) {
		super(name, port, proxyList);
	}
	
	public DPDRegister(String name, int port, Container proxyList, boolean verbose) {
		super(name, port, proxyList, verbose);
	}

	@Override
	public RegisterConnection<Container> createConnection(Socket socket, Container proxyRefs) {
		return new RegisterConnection<Container>( socket, proxyRefs );
	}
}
