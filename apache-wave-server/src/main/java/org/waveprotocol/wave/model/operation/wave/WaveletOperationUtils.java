package org.waveprotocol.wave.model.operation.wave;

import java.util.List;

import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.version.HashedVersion;
import org.waveprotocol.wave.model.wave.ParticipantId;

public final class WaveletOperationUtils {

	  /**
	   * Clones a wavelet operation, attaching a new context.
	   * disrvptor - extracted from WaveletOperation
	   */
	  // This might be better as a member method, but is less code this way.
	  public static WaveletOperation cloneOp(WaveletOperation op, WaveletOperationContext newContext) {
	    if (op instanceof NoOp) {
	      return new NoOp(newContext);
	    } else if (op instanceof AddParticipant) {
	      return new AddParticipant(newContext, ((AddParticipant) op).getParticipantId());
	    } else if (op instanceof RemoveParticipant) {
	      return new RemoveParticipant(newContext, ((RemoveParticipant) op).getParticipantId());
	    } else if (op instanceof WaveletBlipOperation) {
	      String docId = ((WaveletBlipOperation) op).getBlipId();
	      // BlipContentOperation is the only expected blip op type.
	      BlipContentOperation blipOp = (BlipContentOperation) ((WaveletBlipOperation) op).getBlipOp();
	      return new WaveletBlipOperation(docId, new BlipContentOperation(newContext,
	          blipOp.getContentOp()));
	    } else {
	      throw new IllegalArgumentException("Un-cloneable operation: " + op);
	    }
	  }

	  /**
	   * Clones ops from a client delta to a transformed delta, replacing their
	   * contexts with that implied by the delta's metadata.
	   * disrvptor - extracted from TransformedWaveletDelta
	   */
	  public static TransformedWaveletDelta cloneOperations(HashedVersion resultingVersion,
	      long applicationTimestamp, WaveletDelta delta) {
	    return cloneOperations(delta.getAuthor(), resultingVersion, applicationTimestamp, delta);
	  }

	  /**
	   * Clones a list of operations into a transformed delta, replacing their
	   * contexts with that implied by the delta's metadata.
	   * disrvptor - extracted from TransformedWaveletDelta
	   */
	  public static TransformedWaveletDelta cloneOperations(ParticipantId author,
	      HashedVersion resultingVersion, long applicationTimestamp,
	      List<? extends WaveletOperation> ops) {
	    List<WaveletOperation> transformedOps = CollectionUtils.newArrayList();
	    WaveletOperationContext initialContext =
	        new WaveletOperationContext(author, applicationTimestamp, 1);
	    for (int i = 0; i < ops.size() - 1; ++i) {
	      transformedOps.add(WaveletOperationUtils.cloneOp(ops.get(i), initialContext));
	    }
	    if (ops.size() > 0) {
	      WaveletOperationContext finalContext =
	        new WaveletOperationContext(author, applicationTimestamp, 1, resultingVersion);
	      transformedOps.add(WaveletOperationUtils.cloneOp(ops.get(ops.size() - 1), finalContext));
	    }
	    return new TransformedWaveletDelta(author, resultingVersion, applicationTimestamp,
	        transformedOps);
	  }

}
