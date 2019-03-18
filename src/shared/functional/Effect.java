package shared.functional;

public interface Effect<T> {
	public void apply(T c);
}
