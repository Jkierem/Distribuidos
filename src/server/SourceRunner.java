package server;

import java.io.IOException;

public class SourceRunner {

	public static void main(String[] args) {
		if( args[0].equals("help") ) {
			System.out.println("Usage: Source <port> <path to petition list>");
		} else {			
			int port = Integer.parseInt(args[0]); 
			String path = args[1];
			try {
				new SourceServer("Source", port, path).start();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
