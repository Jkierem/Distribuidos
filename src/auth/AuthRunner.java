package auth;

import java.io.IOException;

public class AuthRunner {

	public static void main(String[] args) {
		String path = "C:\\Users\\Juan\\Documents\\ProyectoDistri\\Distribuidos\\src\\auth\\userList";
		int port = 8001;
		AuthServer auth;
		try {
			auth = new AuthServer(path, port, true);
			auth.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
