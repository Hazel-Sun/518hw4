package dsl;

import utils.functions.Func2;

import java.util.ArrayDeque;
import java.util.Queue;

// A variant of parallel composition, which is similar to 'zip'.

public class Parallel<A, B, C, D> implements Query<A, D> {

	private final Query<A, B> q1;
	private final Query<A, C> q2;
	private final Func2<B, C, D> op;

	private final Sink<B> sinkB = new Sink<B>() {
		@Override
		public void next(B item) {
			b.add(item);
		}

		@Override
		public void end() {}
	};

	private final Sink<C> sinkC = new Sink<C>() {
		@Override
		public void next(C item) {
			c.add(item);
		}

		@Override
		public void end() {

		}
	};


	private Queue<B> b;
	private Queue<C> c;


	public Parallel(Query<A,B> q1, Query<A,C> q2, Func2<B,C,D> op) {
		this.q1 = q1;
		this.q2 = q2;
		this.op = op;
		this.b = new ArrayDeque<>();
		this.c = new ArrayDeque<>();
	}

	@Override
	public void start(Sink<D> sink) {
	}

	@Override
	public void next(A item, Sink<D> sink) {
		q1.next(item, sinkB);
		q2.next(item, sinkC);
		if (!b.isEmpty() && !c.isEmpty()){
			sink.next(op.apply(b.poll(), c.poll()));
		}


	}

	@Override
	public void end(Sink<D> sink) {
		// TODO
	}
	
}
