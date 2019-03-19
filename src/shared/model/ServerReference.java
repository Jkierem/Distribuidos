package shared.model;

public class ServerReference {
	private String address;
	private int port;
	
	public ServerReference(String socketStr) {
		String[] tokens = socketStr.split(":");
		this.address = tokens[0];
		this.port = Integer.parseInt(tokens[1]);
	}
	
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
	
	public boolean equals( ServerReference other ) {
		return other.address.compareTo(this.address) == 0 && other.port == this.port;
	}
	
	public static ServerReference empty() {
		return new ServerReference("",-1);
	}
	
	public boolean isNullRef() {
		return this.equals(ServerReference.empty());
	}
}
