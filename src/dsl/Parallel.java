// package dsl;

// import utils.functions.Func2;

// // A variant of parallel composition, which is similar to 'zip'.

// public class Parallel<A, B, C, D> implements Query<A, D> {

// 	// TODO

// 	public Parallel(Query<A,B> q1, Query<A,C> q2, Func2<B,C,D> op) {
// 		// TODO
// 	}

// 	@Override
// 	public void start(Sink<D> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void next(A item, Sink<D> sink) {
// 		// TODO
// 	}

// 	@Override
// 	public void end(Sink<D> sink) {
// 		// TODO
// 	}
	
// }
// package dsl;

// import java.util.LinkedList;
// import java.util.Queue;

// import utils.functions.Func2;

// // A variant of parallel composition, which is similar to 'zip'.
// public class Parallel<A, B, C, D> implements Query<A, D> {

// 	private final Query<A, B> q1;
// 	private final Query<A, C> q2;
// 	private final Func2<B, C, D> op;

// 	private final Queue<B> buf1 = new LinkedList<>();
// 	private final Queue<C> buf2 = new LinkedList<>();

// 	private Sink<B> q1Sink;
// 	private Sink<C> q2Sink;

// 	public Parallel(Query<A, B> q1, Query<A, C> q2, Func2<B, C, D> op) {
// 		this.q1 = q1;
// 		this.q2 = q2;
// 		this.op = op;
// 	}

// 	@Override
// 	public void start(Sink<D> sink) {
// 		q1Sink = new Sink<B>() {
// 			@Override
// 			public void next(B item) {
// 				buf1.add(item);
// 				tryEmit(sink);
// 			}
// 			@Override
// 			public void end() {
// 				// No special cleanup
// 			}
// 		};

// 		q2Sink = new Sink<C>() {
// 			@Override
// 			public void next(C item) {
// 				buf2.add(item);
// 				tryEmit(sink);
// 			}
// 			@Override
// 			public void end() {
// 				// No special cleanup
// 			}
// 		};

// 		q1.start(q1Sink);
// 		q2.start(q2Sink);
// 	}

// 	@Override
// 	public void next(A item, Sink<D> sink) {
// 		q1.next(item, q1Sink);
// 		q2.next(item, q2Sink);
// 	}

// 	@Override
// 	public void end(Sink<D> sink) {
// 		q1.end(q1Sink);
// 		q2.end(q2Sink);
// 	}

// 	private void tryEmit(Sink<D> sink) {
// 		if (!buf1.isEmpty() && !buf2.isEmpty()) {
// 			B b = buf1.poll();
// 			C c = buf2.poll();
// 			D d = op.apply(b, c);
// 			sink.next(d);
// 		}
// 	}
// }
package dsl;

import java.util.ArrayDeque;
import java.util.Queue;
import utils.functions.Func2;

// A variant of parallel composition, which is similar to 'zip'.
public class Parallel<A, B, C, D> implements Query<A, D> {

	private final Query<A, B> q1;
	private final Query<A, C> q2;
	private final Func2<B, C, D> op;

	private Sink<D> sinkD;

	// 每个输入项都可能生成一个 result
	private Queue<B> bufferB = new ArrayDeque<>();
	private Queue<C> bufferC = new ArrayDeque<>();

	private Sink<B> sinkB;
	private Sink<C> sinkC;

	public Parallel(Query<A, B> q1, Query<A, C> q2, Func2<B, C, D> op) {
		this.q1 = q1;
		this.q2 = q2;
		this.op = op;
	}

	@Override
	public void start(Sink<D> sink) {
		this.sinkD = sink;

		this.sinkB = new Sink<B>() {
			@Override
			public void next(B item) {
				bufferB.add(item);
				maybeEmit();
			}

			@Override
			public void end() {}
		};

		this.sinkC = new Sink<C>() {
			@Override
			public void next(C item) {
				bufferC.add(item);
				maybeEmit();
			}

			@Override
			public void end() {}
		};

		q1.start(sinkB);
		q2.start(sinkC);
	}

	@Override
	public void next(A item, Sink<D> sink) {
		q1.next(item, sinkB);
		q2.next(item, sinkC);
	}

	private void maybeEmit() {
		while (!bufferB.isEmpty() && !bufferC.isEmpty()) {
			B b = bufferB.poll();
			C c = bufferC.poll();
			D d = op.apply(b, c);
			sinkD.next(d);
		}
	}

	@Override
	public void end(Sink<D> sink) {
		q1.end(sinkB);
		q2.end(sinkC);
		sink.end();
	}
}
