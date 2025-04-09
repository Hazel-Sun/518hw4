// package dsl;

// import utils.functions.Func2;

// // Sliding window of size 2.

// public class SWindow2<A,B> implements Query<A,B> {

// 	// TODO

// 	public SWindow2(Func2<A,A,B> op) {
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

import utils.functions.Func2;

// Sliding window of size 2.
public class SWindow2<A,B> implements Query<A,B> {

	private final Func2<A, A, B> op;
	private A prev;
	private boolean hasPrev;

	public SWindow2(Func2<A,A,B> op) {
		this.op = op;
		this.hasPrev = false;
	}

	@Override
	public void start(Sink<B> sink) {
		hasPrev = false;
		prev = null; 
	}
	
	@Override
	public void next(A item, Sink<B> sink) {
		if (hasPrev) {
			B result = op.apply(prev, item);
			sink.next(result);
		}
		prev = item;
		hasPrev = true;
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
}
