// package dsl;

// // Emit a value in the beginning (n times) and then echo
// // the input stream.

// public class Emit<A> implements Query<A,A> {

// 	// TODO

// 	public Emit(int n, A value) {
// 		if (n < 0) {
// 			throw new IllegalArgumentException("Emit: n must be >= 0");
// 		}

// 		// TODO
// 	}

// 	@Override
// 	public void start(Sink<A> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void next(A item, Sink<A> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void end(Sink<A> sink) {
// 		// TODO
// 	}
	
// }
package dsl;

// Emit a value in the beginning (n times) and then echo
// the input stream.
public class Emit<A> implements Query<A,A> {

	private final int n;
	private final A value;
	private boolean emitted = false;

	public Emit(int n, A value) {
		if (n < 0) {
			throw new IllegalArgumentException("Emit: n must be >= 0");
		}
		this.n = n;
		this.value = value;
	}

	@Override
	public void start(Sink<A> sink) {
		if (!emitted) {
			for (int i = 0; i < n; i++) {
				sink.next(value); // Emit n values first
			}
			emitted = true;
		}
	}

	@Override
	public void next(A item, Sink<A> sink) {
		sink.next(item); // Pass through original stream
	}

	@Override
	public void end(Sink<A> sink) {
		sink.end(); // Notify downstream
	}
}
