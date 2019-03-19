package proxy;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import shared.model.ServerReference;
import shared.utils.CSVReader;
import shared.utils.Utils;

public class ProxyRunner {

	public static void main(String[] args) {
		if( args[0].equals("help") ) {
			System.out.println("Usage: Proxy <port> <dpdHost>:<dpdRegistryPort> <path to server list>");
		} else {			
			String localAddress = "localhost";
			try {
				localAddress = Utils.getLocalAddress();
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("Ip address: "+localAddress);
			int proxyPort = Integer.parseInt(args[0]);
			String dnsRef = args[1];
			String path = args[2];
			List<String[]> refs = new ArrayList<String[]>();
			try {
				refs = new CSVReader(path).readFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<ServerReference> sources = new ArrayList<>();
			for( String[] ref : refs ) {			
				sources.add(new ServerReference(ref[0], Integer.parseInt(ref[1])));
			}
			new ProxyServer( 
					new ServerReference( localAddress , proxyPort) ,
					new ServerReference( dnsRef ),
					sources, true
					).start();
		}
	}

}
