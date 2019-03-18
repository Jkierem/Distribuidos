package shared.model;

public class Credentials {
	
	String user;
	String password;
	
	public Credentials() {}
	public Credentials setUser( String user ) {
		this.user = user;
		return this;
	}
	public Credentials setPassword( String password ) {
		this.password = password;
		return this;
	}
	public String toCSVString() {
		return user + "," + password;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public static Credentials empty() {
		return new Credentials()
				.setUser("")
				.setPassword("");
	}
	public boolean isEmpty() {
		return this.equals(Credentials.empty()) == 0;
	}
	public int equals(Credentials other) {
		return this.password.compareTo(other.password) + this.user.compareTo(other.user);
	}
	public static Credentials fromCSVString( String creds ) {
		String[] tokens = creds.split(",");
		if( tokens.length < 2 ) {
			return Credentials.empty();
		}
		return new Credentials()
				.setUser(tokens[0])
				.setPassword(tokens[1]);
	}
	public String toAuthMessage( ) {
		return Operation.AUTH.toString()+":"+this.toCSVString();
	}
}
