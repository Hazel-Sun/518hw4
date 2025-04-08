// package dsl;

// import java.util.function.Function;

// // Apply a function elementwise.

// public class Map<A,B> implements Query<A,B> {

// 	// TODO

// 	public Map(Function<A,B> op) {
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

import java.util.function.Function;

// Apply a function elementwise.
public class Map<A,B> implements Query<A,B> {

	private final Function<A,B> op;

	public Map(Function<A,B> op) {
		this.op = op;
	}

	@Override
	public void start(Sink<B> sink) {
		// 不需要做任何启动前处理
	}

	@Override
	public void next(A item, Sink<B> sink) {
		B result = op.apply(item); // 对输入应用函数
		sink.next(result);         // 将结果传给下游
	}

	@Override
	public void end(Sink<B> sink) {
		sink.end(); // 通知下游结束
	}
}
