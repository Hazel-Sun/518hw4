package dsl;

// Serial composition.

public class Pipeline<A,B,C> implements Query<A,C> {

	private final Query<A, B> q1;
	private final Query<B, C> q2;

	public Pipeline(Query<A,B> q1, Query<B,C> q2) {
		this.q1 = q1;
		this.q2 = q2;
	}

	@Override
	public void start(Sink<C> sink) {
		q1.start(new Sink<B>() {
			@Override
			public void next(B item) {
				q2.next(item, sink);
			}

			@Override
			public void end() {
				q2.end(sink);
			}
		});
		q2.start(sink);
	}

	@Override
	public void next(A item, Sink<C> sink) {
		q1.next(item, new Sink<B>() {
			@Override
			public void next(B item) {
				q2.next(item, sink);
			}

			@Override
			public void end() {
			}
		});
	}

	@Override
	public void end(Sink<C> sink) {
		q1.end(new Sink<B>() {
			@Override
			public void next(B item) {

			}

			@Override
			public void end() {
				q2.end(sink);
			}
		});
	}
	
}
