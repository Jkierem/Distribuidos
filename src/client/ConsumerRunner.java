package client;

import java.util.Scanner;

import shared.model.ServerReference;

public class ConsumerRunner {

	public static void main(String[] args) {
		if( args[0].equals("help") ) {
			System.out.println("Usage: Client <authHost>:<authPort> <dpdHost>:<dpdServicePort>");
		} else {			
			ServerReference authRef = new ServerReference(args[0]);
			ServerReference dnsRef = new ServerReference(args[1]);
			Scanner in = new Scanner(System.in);
			Consumer client = new Consumer(1,authRef,dnsRef,in);
			client.start();
		}
	}

}
