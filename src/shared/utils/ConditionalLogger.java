package shared.utils;

public class ConditionalLogger {
	
	private boolean verbose;
	private String prepend;
	
	public ConditionalLogger() {
		this.verbose = false;
		this.prepend = "";
	}
	
	public ConditionalLogger( boolean verbose ) {
		this.verbose = verbose;
		this.prepend = "";
	}
	
	public ConditionalLogger( boolean verbose , String prepend ) {
		this.verbose = verbose;
		this.prepend = prepend;
	}
	
	public void printIf( String message , boolean condition ) {
		if( condition ) {
			System.out.println(message);
		}
	}
	
	public void log( String message ) {
		printIf( prepend + message , this.verbose );
	}
}
