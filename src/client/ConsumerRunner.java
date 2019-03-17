package client;

import java.util.Scanner;

import shared.model.ServerReference;

public class ConsumerRunner {

	public static void main(String[] args) {
		ServerReference authRef = new ServerReference("localhost", 8001);
		ServerReference dnsRef = new ServerReference("localhost",7778);
		Scanner in = new Scanner(System.in);
		Consumer client = new Consumer(1,authRef,dnsRef,in);
		client.start();
	}

}
