package server;

import java.io.IOException;

public class SourceRunner {

	public static void main(String[] args) {
		int port = 60100;
		String path = "C:\\\\Users\\\\Juan\\\\Documents\\\\ProyectoDistri\\Distribuidos\\src\\server\\propuestas.txt";
		try {
			new SourceServer("Source", port, path).start();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
