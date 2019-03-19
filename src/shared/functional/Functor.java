package shared.functional;

public interface Functor<A,B> {
	public B apply( A x );
}
