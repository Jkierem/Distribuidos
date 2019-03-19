package shared.functional;

public interface BinaryEffect<A,B> {
	public void apply( A a , B b );
}
