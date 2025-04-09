package dsl;

import utils.Or;
import java.util.LinkedList;
import java.util.Queue;

public class Loop<A,B> implements Query<A,B> {
    
    private final Query<Or<A,B>,B> innerQuery;
    private LoopSink loopSink;
    
    public Loop(Query<Or<A,B>,B> q) {
        this.innerQuery = q;
    }

    @Override
    public void start(Sink<B> sink) {
        loopSink = new LoopSink(sink);
        innerQuery.start(loopSink);
        
        processPendingFeedback();
    }

    @Override
    public void next(A item, Sink<B> sink) {
        innerQuery.next(Or.inl(item), loopSink);
        
        processPendingFeedback();
    }

    @Override
    public void end(Sink<B> sink) {
        innerQuery.end(loopSink);
    }
    
    private void processPendingFeedback() {
        while (!loopSink.itemQueue.isEmpty() && !loopSink.terminated) {
            B item = loopSink.itemQueue.poll();
            innerQuery.next(Or.inr(item), loopSink);
        }
    }
    
    
    private class LoopSink implements Sink<B> {
        private final Sink<B> outSink;
        private volatile boolean terminated = false;
        private Queue<B> itemQueue = new LinkedList<>();
        
        public LoopSink(Sink<B> outSink) {
            this.outSink = outSink;
        }
        
        @Override
        public void next(B item) {
            outSink.next(item);
            
            if (!terminated) {
                itemQueue.add(item);
            }
        }
        
        @Override
        public void end() {
            terminated = true;
            itemQueue.clear();
            outSink.end();
        }
    }
}