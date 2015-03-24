package com.google.wave.api;

import java.util.List;
import java.util.Set;

import org.waveprotocol.wave.media.model.AttachmentId;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;

import com.google.wave.api.impl.RawAttachmentData;

public interface OperationQueue {

	/**
	 * Returns a list of the pending operations that have been queued up.
	 *
	 * @return the pending operations.
	 */
	public List<OperationRequest> getPendingOperations();

	/**
	 * Returns the id for {@code proxyingFor} parameter.
	 *
	 * @return the proxying id.
	 */
	public String getProxyForId();

	/**
	 * Creates a view of this {@link OperationQueue} with the proxying for set to
	 * the given id.
	 *
	 * This method returns a new instance of an operation queue that shares the
	 * operation list, but has a different {@link #proxyForId} set so when the
	 * robot uses this new queue, subsequent operations will be sent out with the
	 * {@code proxying_for} field set.
	 *
	 * @param proxyForId the proxying information.
	 * @return a view of this {@link OperationQueue} with the proxying information
	 *     set.
	 */
	public OperationQueue proxyFor(String proxyForId);

	/**
	 * Clears this operation queue.
	 */
	public void clear();

	/**
	 * Appends a blip to a wavelet.
	 *
	 * @param wavelet the wavelet to append the new blip to.
	 * @param initialContent the initial content of the new blip.
	 * @return an instance of {@link Blip} that represents the new blip.
	 */
	public Blip appendBlipToWavelet(Wavelet wavelet, String initialContent);

	/**
	 * Adds a participant to a wavelet.
	 *
	 * @param wavelet the wavelet that the new participant should be added to.
	 * @param participantId the id of the new participant.
	 */
	public void addParticipantToWavelet(Wavelet wavelet, String participantId);

	/**
	 * Removes a participant from a wavelet.
	 *
	 * @param wavelet the wavelet that the participant should be removed from.
	 * @param participantId the id of the participant to be removed.
	 */
	public void removeParticipantFromWavelet(Wavelet wavelet,
			String participantId);

	/**
	 * Creates a new wavelet.
	 *
	 * @param domain the domain to create the wavelet in.
	 * @param participants the initial participants on this new wavelet.
	 * @return an instance of {@link Wavelet} that represents the new wavelet.
	 */
	public Wavelet createWavelet(String domain, Set<String> participants);

	/**
	 * Creates a new wavelet with an optional message.
	 *
	 * @param domain the domain to create the wavelet in.
	 * @param participants the initial participants on this new wavelet.
	 * @param message an optional payload that is returned with the corresponding
	 *     event.
	 * @return an instance of {@link Wavelet} that represents the new wavelet.
	 */
	public Wavelet createWavelet(String domain, Set<String> participants,
			String message);

	/**
	 * Appends search operation for specified query.
	 *
	 * @param query the query to execute.
	 * @param index the index from which to return results.
	 * @param numresults the number of results to return.
	 */
	public void search(String query, Integer index, Integer numresults);

	/**
	 * Sets a key-value pair on the data document of a wavelet.
	 *
	 * @param wavelet to set the data document on.
	 * @param name the name of this data.
	 * @param value the value of this data.
	 */
	public void setDatadocOfWavelet(Wavelet wavelet, String name, String value);

	/**
	 * Requests a snapshot of the specified wave.
	 *
	 * @param waveId the id of the wave that should be fetched.
	 * @param waveletId the wavelet id that should be fetched.
	 */
	public void fetchWavelet(WaveId waveId, WaveletId waveletId);

	/**
	 * Retrieves list of wave wavelets ids.
	 *
	 * @param waveId the id of the wave.
	 */
	public void retrieveWaveletIds(WaveId waveId);

	/**
	 * Exports snapshot of wavelet.
	 *
	 * @param waveId the id of the wave that should be exported.
	 * @param waveletId the id of the wavelet that should be exported.
	 */
	public void exportSnapshot(WaveId waveId, WaveletId waveletId);

	/**
	 * Exports deltas of wavelet.
	 *
	 * @param waveId the id of the wave that should be exported.
	 * @param waveletId the id of the wavelet that should be exported.
	 * @param fromVersion start version.
	 * @param toVersion to version.
	 */
	public void exportRawDeltas(WaveId waveId, WaveletId waveletId,
			byte[] fromVersion, byte[] toVersion);

	/**
	 * Export attachment.
	 *
	 * @param attachmentId the id of attachment.
	 */
	public void exportAttachment(AttachmentId attachmentId);

	/**
	 * Imports deltas of wavelet.
	 *
	 * @param waveId the id of the wave that should be imported.
	 * @param waveletId the id of the wavelet that should be imported.
	 * @param history the history in deltas.
	 */
	public void importRawDeltas(WaveId waveId, WaveletId waveletId,
			List<byte[]> history);

	/**
	 * Imports attachment.
	 *
	 * @param waveId the id of the wave that should be imported.
	 * @param waveletId the id of the wavelet that should be imported.
	 * @param attachmentId the id of attachment.
	 * @param attachmentData the attachment data.
	 */
	public void importAttachment(WaveId waveId, WaveletId waveletId,
			AttachmentId attachmentId, RawAttachmentData attachmentData);

	/**
	 * Sets the title of a wavelet.
	 *
	 * @param wavelet the wavelet whose title will be changed.
	 * @param title the new title to be set.
	 */
	public void setTitleOfWavelet(Wavelet wavelet, String title);

	/**
	 * Modifies a tag in a wavelet.
	 *
	 * @param wavelet the wavelet to modify the tag from.
	 * @param tag the name of the tag to be modified
	 * @param modifyHow how to modify the tag. The default behavior is to add the
	 *     tag. Specify {@code remove} to remove, or specify {@code null} or
	 *     {@code add} to add.
	 */
	public void modifyTagOfWavelet(Wavelet wavelet, String tag, String modifyHow);

	/**
	 * Creates a child blip of another blip.
	 *
	 * @param blip the parent blip.
	 * @return an instance of {@link Blip} that represents the new child blip.
	 */
	public Blip createChildOfBlip(Blip blip);

	/**
	 * Appends a new blip to the end of the thread of the given blip.
	 *
	 * @param blip the blip whose thread will be appended.
	 * @return an instance of {@link Blip} that represents the new blip.
	 */
	public Blip continueThreadOfBlip(Blip blip);

	/**
	 * Deletes the specified blip.
	 *
	 * @param wavelet the wavelet that owns the blip.
	 * @param blipId the id of the blip that will be deleted.
	 */
	public void deleteBlip(Wavelet wavelet, String blipId);

	/**
	 * Appends content with markup to a blip.
	 *
	 * @param blip the blip where this markup content should be added to.
	 * @param content the markup content that should be added to the blip.
	 */
	public void appendMarkupToDocument(Blip blip, String content);

	/**
	 * Submits this operation queue when the given {@code other} operation queue
	 * is submitted.
	 *
	 * @param other the other operation queue to merge this operation queue with.
	 */
	public void submitWith(OperationQueue other);

	/**
	 * Creates and queues a document modify operation.
	 *
	 * @param blip the blip to modify.
	 * @return an instance of {@code OperationRequest} that represents this
	 *     operation. The caller of this method should append the required and/or
	 *     optional parameters, such as:
	 *     <ul>
	 *       <li>{@code modifyAction}</li>
	 *       <li>{@code modifyQuery}</li>
	 *       <li>{@code index}</li>
	 *       <li>{@code range}</li>
	 *     </ul>
	 */
	public OperationRequest modifyDocument(Blip blip);

	/**
	 * Inserts a new inline blip at a specified location.
	 *
	 * @param blip the blip to anchor this inline blip from.
	 * @param position the position in the given blip to insert this new inline
	 *     blip.
	 * @return an instance of {@link Blip} that represents the inline blip.
	 */
	public Blip insertInlineBlipToDocument(Blip blip, int position);

	/**
	 * Modifies the role of a participant in a wavelet.
	 *
	 * @param wavelet the wavelet that the participant is on
	 * @param participant whose role to modify
	 * @param role to set for the participant
	 */
	public void modifyParticipantRoleOfWavelet(Wavelet wavelet,
			String participant, String role);

	/**
	 * Notifies the robot information.
	 *
	 * @param protocolVersion the wire protocol version of the robot.
	 * @param capabilitiesHash the capabilities hash of the robot.
	 */
	public void notifyRobotInformation(ProtocolVersion protocolVersion,
			String capabilitiesHash);

	public void fetchProfiles(FetchProfilesRequest request);

}