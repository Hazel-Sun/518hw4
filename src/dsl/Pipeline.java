// package dsl;

// // Serial composition.

// public class Pipeline<A,B,C> implements Query<A,C> {

// 	// TODO

// 	public Pipeline(Query<A,B> q1, Query<B,C> q2) {
// 		// TODO
// 	}

// 	@Override
// 	public void start(Sink<C> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void next(A item, Sink<C> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void end(Sink<C> sink) {
// 		// TODO
// 	}
	
// }
package dsl;

public class Pipeline<A,B,C> implements Query<A,C> {

	private final Query<A,B> q1;
	private final Query<B,C> q2;
	private Sink<B> intermediateSink;

	public Pipeline(Query<A,B> q1, Query<B,C> q2) {
		this.q1 = q1;
		this.q2 = q2;
	}

	@Override
	public void start(Sink<C> sink) {
		// 定义中间 sink：q1 输出进入 q2
		intermediateSink = new Sink<B>() {
			@Override
			public void next(B item) {
				q2.next(item, sink); // 把 q1 输出交给 q2 处理
			}
			@Override
			public void end() {
				q2.end(sink);
			}
		};

		q2.start(sink);         // 先启动 q2
		q1.start(intermediateSink); // 启动 q1，并接入中间 sink
	}

	@Override
	public void next(A item, Sink<C> sink) {
		q1.next(item, intermediateSink); // ⚠️ 不再传 null，使用真实 sink
	}

	@Override
	public void end(Sink<C> sink) {
		q1.end(intermediateSink);
	}
}
