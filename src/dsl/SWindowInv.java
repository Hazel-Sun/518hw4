// package dsl;

// import java.util.function.BinaryOperator;

// // Efficient algorithm for aggregation over a sliding window.
// // It assumes that there is a 'remove' operation for updating
// // the aggregate when an element is evicted from the window.

// public class SWindowInv<A> implements Query<A,A> {

// 	// TODO

// 	public SWindowInv
// 	(int wndSize, A init, BinaryOperator<A> insert, BinaryOperator<A> remove)
// 	{
// 		if (wndSize < 1) {
// 			throw new IllegalArgumentException("window size should be >= 1");
// 		}
		
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

import java.util.LinkedList;
import java.util.function.BinaryOperator;

// Efficient algorithm for aggregation over a sliding window.
public class SWindowInv<A> implements Query<A, A> {

	private final int wndSize;
	private final A init;
	private final BinaryOperator<A> insert;
	private final BinaryOperator<A> remove;

	private A acc;
	private LinkedList<A> window;

	public SWindowInv(int wndSize, A init,
	                 BinaryOperator<A> insert,
	                 BinaryOperator<A> remove)
	{
		if (wndSize < 1) {
			throw new IllegalArgumentException("window size should be >= 1");
		}
		this.wndSize = wndSize;
		this.init = init;
		this.insert = insert;
		this.remove = remove;
		this.window = new LinkedList<>();
		this.acc = init;
	}

	@Override
	public void start(Sink<A> sink) {
		window.clear();
		acc = init;
	}

	@Override
	public void next(A item, Sink<A> sink) {
		// 插入新元素
		window.addLast(item);
		acc = insert.apply(acc, item);

		// 超出窗口大小，移除最早的元素
		if (window.size() > wndSize) {
			A evicted = window.removeFirst();
			acc = remove.apply(acc, evicted);
		}

		// 当前窗口够大才输出
		if (window.size() == wndSize) {
			sink.next(acc);
		}
	}

	@Override
	public void end(Sink<A> sink) {
		sink.end();
	}
}
