package shared.model;

public class ServerReference {
	private String address;
	private int port;
	
	public ServerReference(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public static ServerReference fromCSVString( String info ) {
		String[] tokens = info.split(","); 
		String address = tokens[0];
		int port = Integer.parseInt(tokens[1]);
		return new ServerReference(address, port);
	}
	
	public String toCSVString() {
		return this.address + "," + String.valueOf(this.port);
	}
	
}
