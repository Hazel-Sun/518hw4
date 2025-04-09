package dsl;

import utils.functions.Func2;

// Aggregation (one output item when the stream ends).

public class Fold<A, B> implements Query<A, B> {

	private B value;

	private final Func2<B,A,B> op;
	public Fold(B init, Func2<B,A,B> op) {
		this.value = init;
		this.op = op;
	}

	@Override
	public void start(Sink<B> sink) {

	}

	@Override
	public void next(A item, Sink<B> sink) {
		this.value = op.apply(value, item);
	}

	@Override
	public void end(Sink<B> sink) {
		sink.next(value);
		sink.end();
	}
	
}
