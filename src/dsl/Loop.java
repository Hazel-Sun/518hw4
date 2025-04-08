// package dsl;

// import utils.Or;

// // Feedback composition.

// public class Loop<A,B> implements Query<A,B> {

// 	// TODO

// 	public Loop(Query<Or<A,B>,B> q) {
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

import utils.Or;
import java.util.ArrayDeque;
import java.util.Queue;

public class Loop<A, B> implements Query<A, B> {

    private final Query<Or<A, B>, B> q;
    private Sink<B> outerSink;
    private final Queue<B> feedbackQueue = new ArrayDeque<>();
    private boolean isProcessing = false;

    public Loop(Query<Or<A, B>, B> q) {
        this.q = q;
    }

    private final Sink<B> feedbackSink = new Sink<B>() {
        @Override
        public void next(B item) {
            outerSink.next(item);
            feedbackQueue.add(item);
            processFeedback();
        }

        @Override
        public void end() {
            // Do not end the outer sink here
        }
    };

    @Override
    public void start(Sink<B> sink) {
        this.outerSink = sink;
        System.out.println("Loop: outerSink set");
        q.start(feedbackSink);
        System.out.println("Loop: feedbackQuery started");
    }
    
    @Override
    public void next(A item, Sink<B> ignored) {
        q.next(Or.inl(item), feedbackSink);
    }

    private void processFeedback() {
        if (isProcessing) return;
        isProcessing = true;
        try {
            while (!feedbackQueue.isEmpty()) {
                B item = feedbackQueue.poll();
                q.next(Or.inr(item), feedbackSink);
            }
        } finally {
            isProcessing = false;
        }
    }

    @Override
    public void end(Sink<B> sink) {
        q.end(feedbackSink);
    }
}
