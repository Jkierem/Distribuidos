package client;

import java.util.Scanner;

import shared.model.ServerReference;

public class ConsumerRunner {

	public static void main(String[] args) {
		ServerReference dnsRef = new ServerReference("localhost",7778);
		Scanner in = null;
		in = new Scanner(System.in);
		String input = "";
		int clients = 0;
		while( input.compareTo("exit") != 0 ) {
			input = in.nextLine();
			if( input.compareTo("create") == 0 ) {
				clients++;
				System.out.println(clients);
				Consumer client = new Consumer(dnsRef);
				client.start();
			}
		}
		in.close();
	}

}
