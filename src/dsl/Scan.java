// package dsl;

// import utils.functions.Func2;

// // Running aggregation (one output item per input item).

// public class Scan<A, B> implements Query<A, B> {

// 	// TODO

// 	public Scan(B init, Func2<B,A,B> op) {
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

// Running aggregation (one output item per input item).
public class Scan<A, B> implements Query<A, B> {

	private final B init;
	private final Func2<B, A, B> op;
	private B acc;

	public Scan(B init, Func2<B, A, B> op) {
		this.init = init;
		this.op = op;
		this.acc = init;
	}

	@Override
	public void start(Sink<B> sink) {
		acc = init;
	}

	@Override
	public void next(A item, Sink<B> sink) {
		acc = op.apply(acc, item);
		sink.next(acc); // 每步都输出当前累积值
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end();
	}
}
