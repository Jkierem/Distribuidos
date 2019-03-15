package shared.utils;

public class ConditionalLogger {
	
	private boolean verbose;
	
	public ConditionalLogger() {
		this.verbose = false;
	}
	
	public ConditionalLogger( boolean verbose ) {
		this.verbose = verbose;
	}
	
	public void printIf( String message , boolean condition ) {
		if( condition ) {
			System.out.println(message);
		}
	}
	
	public void log( String message ) {
		printIf( message , this.verbose );
	}
}
