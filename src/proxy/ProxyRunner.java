package proxy;

import java.util.Scanner;

import shared.model.ServerReference;

public class ProxyRunner {

	public static void main(String[] args) {
		String localAddress = "localhost";
		String dnsAddress = "localhost";
		int dnsRegistryPort = 7777;
		int proxyMaxCount = 10;
		int proxyCount = 0;
		int currentProxyPort = 7000;
		String input = "valid";
		Scanner in = null;
		in = new Scanner(System.in);
		while( input.compareTo("exit") != 0 ) {
			input = in.nextLine();
			if( input.compareTo("create") == 0 ) {
				if( proxyCount < proxyMaxCount ) {					
					new ProxyServer( 
							new ServerReference( localAddress , currentProxyPort) ,
							new ServerReference( dnsAddress , dnsRegistryPort)
						).start();
					proxyCount++;
					currentProxyPort++;
				} 
			}
		}
		in.close();
	}

}
