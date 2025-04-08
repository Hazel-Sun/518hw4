// package dsl;

// import utils.functions.Func2;

// // Aggregation (one output item when the stream ends).

// public class Fold<A, B> implements Query<A, B> {

// 	// TODO

// 	public Fold(B init, Func2<B,A,B> op) {
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

// Aggregation (one output item when the stream ends).
public class Fold<A, B> implements Query<A, B> {

	private final B init;
	private final Func2<B, A, B> op;
	private B acc;

	public Fold(B init, Func2<B, A, B> op) {
		this.init = init;
		this.op = op;
		this.acc = init;
	}

	@Override
	public void start(Sink<B> sink) {
		acc = init; // 每次启动重置累积值
	}

	@Override
	public void next(A item, Sink<B> sink) {
		acc = op.apply(acc, item); // 把 item 加入累计值
	}

	@Override
	public void end(Sink<B> sink) {
		sink.next(acc); // 结束时输出累计值
		sink.end();     // 通知下游结束
	}
}
