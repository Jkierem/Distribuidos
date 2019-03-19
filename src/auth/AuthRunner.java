package auth;

import java.io.IOException;

public class AuthRunner {

	public static void main(String[] args) {
		if( args[0].equals("help") ) {
			System.out.println("Usage: Auth <port> <path to userlist>");
		} else {			
			int port = Integer.parseInt(args[0]);
			String path = args[1];
			AuthServer auth;
			try {
				auth = new AuthServer(path, port, true);
				auth.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
