package org.waveprotocol.wave.concurrencycontrol.client;

import java.util.List;
import java.util.NoSuchElementException;

import org.waveprotocol.wave.model.operation.TransformException;
import org.waveprotocol.wave.model.operation.wave.WaveletOperation;

public interface OperationQueue {

	/**
	 * Adds the given operation to the tail of the operation queue. Merges with
	 * the delta at the tail if the creators match, otherwise creates a new tail
	 * delta.
	 */
	public void add(WaveletOperation op);

	/**
	 * Prepends the given delta onto the queue's head. No merging is
	 * allows on this delta. This is because we don't know if the server have actually got
	 * the previously sent delta, we can't change the delta once it's sent.
	 *
	 * @param newHead delta to use for the queue. Must only contain operations from a
	 *        single author. May be empty, in which case this call will do
	 *        nothing.
	 */
	public void insertHead(List<WaveletOperation> newHead);

	/** Returns true if there are no pending operations in the queue. */
	public boolean isEmpty();

	/**
	 * Estimates the number of operations in this queue.
	 *
	 * In order to provide a bounded execution time the result is an underestimate
	 * of the true number of queued operations.
	 */
	public int estimateSize();

	/**
	 * Takes a delta full of operations by the same creator from the head of the
	 * queue, removing those operations from the queue. It will contain all
	 * operations from the head of the queue up until but not including the first
	 * change in creator. Hence if all operations in the queue have the same
	 * creator, it will contain all those operations and the queue will become
	 * empty.
	 *
	 * @return A non-empty delta without signature or version information,
	 *         containing operations which all have the same creator address in
	 *         their context.
	 * @throws NoSuchElementException If the queue is empty.
	 */
	public List<WaveletOperation> take();

	/**
	 * Transforms the given server delta against the queued operations. Updates
	 * the queued deltas with the results of the transformation and returns the
	 * transformed server delta.
	 *
	 * Queued delta which have all of their operations transformed away and hence
	 * become empty are discarded.
	 *
	 * @throws TransformException If transformation of any operations fails.
	 */
	public List<WaveletOperation> transform(List<WaveletOperation> serverOps)
			throws TransformException;

	public String toString();

}