// package dsl;

// import utils.functions.Func3;

// // Sliding window of size 3.

// public class SWindow3<A,B> implements Query<A,B> {

// 	// TODO

// 	public SWindow3(Func3<A,A,A,B> op) {
// 		// TODO
// 	}

// 	@Override
// 	public void start(Sink<B> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void next(A item, Sink<B> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void end(Sink<B> sink) {
// 		// TODO
// 	}
	
// }
package dsl;

import utils.functions.Func3;

// Sliding window of size 3.
public class SWindow3<A,B> implements Query<A,B> {

	private final Func3<A, A, A, B> op;
	private A a, b;
	private int count;

	public SWindow3(Func3<A, A, A, B> op) {
		this.op = op;
	}

	@Override
	public void start(Sink<B> sink) {
		count = 0;
		a = null;
		b = null;
	}

	@Override
	public void next(A item, Sink<B> sink) {
		if (count < 2) {
			if (count == 0) a = item;
			else b = item;
			count++;
		} else {
			B result = op.apply(a, b, item);
			sink.next(result);
			a = b;
			b = item;
		}
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end(); // optional
	}
}
