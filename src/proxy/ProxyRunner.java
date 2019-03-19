package proxy;

import java.util.ArrayList;
import java.util.List;

import shared.model.ServerReference;

public class ProxyRunner {

	public static void main(String[] args) {
		String localAddress = "localhost";
		String dnsAddress = "localhost";
		int dnsRegistryPort = 7777;
		int currentProxyPort = 7000;
		List<ServerReference> sources = new ArrayList<>();
		sources.add(new ServerReference("localhost", 60100));
		//sources.add(new ServerReference("localhost", 60101));
		//sources.add(new ServerReference("localhost", 60102));
		new ProxyServer( 
				new ServerReference( localAddress , currentProxyPort) ,
				new ServerReference( dnsAddress , dnsRegistryPort),
				sources, true
		).start();
	}

}
