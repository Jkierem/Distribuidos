package shared.functional;

public class Transition<T> implements Effect<T>, Condition<T>{
	
	private Condition<T> condition;
	private Effect<T> effect;
	
	public Transition( Condition<T> condition ) {
		super();
		this.condition = condition;
		this.effect = x -> {};
	}
	
	public Transition( Condition<T> condition, Effect<T> effect ) {
		super();
		this.condition = condition;
		this.effect = effect;
	}
	
	public Transition<T> setCondition( Condition<T> e ) {
		return new Transition<T>( e , this.effect );
	}
	
	public Transition<T> setEffect( Effect<T> e ) {
		return new Transition<T>( this.condition , e );
	}

	@Override
	public void apply(T c) {
		this.effect.apply(c);
	}
	
	@Override
	public boolean holds(T c) {
		return this.condition.holds(c);
	}
}
