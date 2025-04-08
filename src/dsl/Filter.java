// package dsl;

// import java.util.function.Predicate;

// // Filter out elements that falsify the given predicate.

// public class Filter<A> implements Query<A,A> {

// 	// TODO

// 	public Filter(Predicate<A> pred) {
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

import java.util.function.Predicate;

// Filter out elements that falsify the given predicate.
public class Filter<A> implements Query<A,A> {

	private final Predicate<A> pred;

	public Filter(Predicate<A> pred) {
		this.pred = pred;
	}

	@Override
	public void start(Sink<A> sink) {
		// 不需要初始化任何状态
	}

	@Override
	public void next(A item, Sink<A> sink) {
		if (pred.test(item)) {
			sink.next(item);  // 满足条件的才传递下去
		}
	}

	@Override
	public void end(Sink<A> sink) {
		sink.end();
	}
}
