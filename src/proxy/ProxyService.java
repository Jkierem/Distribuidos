package proxy;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import shared.logic.AbstractServer;
import shared.model.ServerReference;

public class ProxyService extends AbstractServer {
	
	ConcurrentHashMap<String, ServerReference> directory;

	public ProxyService(String name, int port, ConcurrentHashMap<String, ServerReference> directory) {
		super(name, port);
		this.directory = directory;
	}

	public ProxyService(String name, int port, ConcurrentHashMap<String, ServerReference> directory ,boolean verbose) {
		super(name, port, verbose);
		this.directory = directory;
	}

	@Override
	public void onRequest(Socket socket) {
		new ProxyConnection(socket,this.directory).start();
	}

}
