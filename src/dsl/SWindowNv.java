// package dsl;

// import utils.functions.Func2;

// // Naive algorithm for aggregation over a sliding window.

// public class SWindowNv<A,B> implements Query<A,B> {

// 	// TODO

// 	public SWindowNv(int wndSize, B init, Func2<B,A,B> op) {
// 		if (wndSize < 1) {
// 			throw new IllegalArgumentException("window size should be >= 1");
// 		}

// 		// TODO
// 	}

// 	@Override
// 	public void start(Sink<B> sink) {
// 		// TODO

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

import java.util.LinkedList;
import utils.functions.Func2;

// Naive algorithm for aggregation over a sliding window.
public class SWindowNv<A, B> implements Query<A, B> {

	private final int wndSize;
	private final B init;
	private final Func2<B, A, B> op;

	private LinkedList<A> window;

	public SWindowNv(int wndSize, B init, Func2<B, A, B> op) {
		if (wndSize < 1) {
			throw new IllegalArgumentException("window size should be >= 1");
		}
		this.wndSize = wndSize;
		this.init = init;
		this.op = op;
		this.window = new LinkedList<>();
	}

	@Override
	public void start(Sink<B> sink) {
		window.clear();
	}

	@Override
	public void next(A item, Sink<B> sink) {
		window.addLast(item);
		if (window.size() > wndSize) {
			window.removeFirst();
		}

		if (window.size() == wndSize) {
			B acc = init;
			for (A x : window) {
				acc = op.apply(acc, x);
			}
			sink.next(acc);
		}
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
}
