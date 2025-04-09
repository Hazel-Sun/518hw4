package dsl;

import java.util.function.Predicate;

// Filter out elements that falsify the given predicate.

public class Filter<A> implements Query<A,A> {

	private final Predicate<A> predicate;

	public Filter(Predicate<A> pred) {
		this.predicate = pred;
	}

	@Override
	public void start(Sink<A> sink) {

	}

	@Override
	public void next(A item, Sink<A> sink) {
		if (predicate.test(item)){
			sink.next(item);
		}
	}

	@Override
	public void end(Sink<A> sink) {
		sink.end();
	}
	
}
