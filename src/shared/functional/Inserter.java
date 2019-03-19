package shared.functional;

public class Inserter<A,B,Container> {
	
	private Container container;
	private Functor<A,B> mapping;
	private BinaryEffect<B,Container> insert;
	
	public Inserter( NullaryFunction<Container> instance, Functor<A,B> mapping , BinaryEffect<B,Container> insert ) {
		this.container = instance.apply();
		this.mapping = mapping;
		this.insert = insert;
	}

	public void insert( A x ) {
		this.insert.apply( this.mapping.apply(x) , this.container );
	}
	
	public Container getData() {
		return this.container;
	}
}
