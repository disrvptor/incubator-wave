package com.google.wave.api;

import java.util.Map;

import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;

import com.google.wave.api.impl.WaveletData;

public interface Wavelet {

	/**
	 * Returns the wave id that owns this wavelet.
	 *
	 * @return the wave id.
	 */
	public WaveId getWaveId();

	/**
	 * Returns the id of this wavelet.
	 *
	 * @return the wavelet id.
	 */
	public WaveletId getWaveletId();

	/**
	 * Returns the participant id of the creator of this wavelet.
	 *
	 * @return the creator of this wavelet.
	 */
	public String getCreator();

	/**
	 * Returns the creation time of this wavelet.
	 *
	 * @return the wavelet's creation time.
	 */
	public long getCreationTime();

	/**
	 * Returns the last modified time of this wavelet.
	 *
	 * @return the wavelet's last modified time.
	 */
	public long getLastModifiedTime();

	/**
	 * Returns the data documents of this wavelet, which is a series of key-value
	 * pairs of data.
	 *
	 * @return an instance of {@link DataDocuments}, which represents the data
	 *     documents of this wavelet.
	 */
	public DataDocuments getDataDocuments();

	/**
	 * Returns a list of participants of this wavelet.
	 *
	 * @return an instance of {@link Participants}, which represents the
	 *     participants of this wavelet.
	 */
	public Participants getParticipants();

	/**
	 * Returns a list of tags of this wavelet.
	 *
	 * @return an instance of {@link Tags}, which represents the tags that have
	 *     been associated with this wavelet.
	 */
	public Tags getTags();

	/**
	 * Returns the title of this wavelet.
	 *
	 * @return the wavelet title.
	 */
	public String getTitle();

	/**
	 * Sets the wavelet title.
	 *
	 * @param title the new title to be set.
	 */
	public void setTitle(String title);

	/**
	 * Returns the address of the robot that receives events or performs events
	 * on this wavelet.
	 *
	 * @return the robot address.
	 */
	public String getRobotAddress();

	/**
	 * Sets the address of the robot that receives events or performs events on
	 * this wavelet.
	 *
	 * @param address the robot address.
	 * @throw IllegalStateException if this method has been called before.
	 */
	public void setRobotAddress(String address);

	/**
	 * Returns the id of the root blip of this wavelet.
	 *
	 * @return the id of the root blip.
	 */
	public String getRootBlipId();

	/**
	 * Returns the root blip of this wavelet.
	 *
	 * @return an instance of {@link Blip} that represents the root blip of this
	 *     wavelet.
	 */
	public Blip getRootBlip();

	/**
	 * @return an instance of {@link BlipThread} that represents the root thread of
	 *     this wavelet.
	 */
	public BlipThread getRootThread();

	/**
	 * Returns a blip with the given id.
	 *
	 * @return an instance of {@link Blip} that has the given blip id.
	 */
	public Blip getBlip(String blipId);

	/**
	 * Returns all blips that are in this wavelet.
	 *
	 * @return a map of blips in this wavelet, that is keyed by blip id.
	 */
	public Map<String, Blip> getBlips();

	/**
	 * Returns a thread with the given id.
	 *
	 * @param threadId the thread id.
	 * @return a thread that has the given thread id.
	 */
	public BlipThread getThread(String threadId);

	/**
	 * Returns all threads that are in this wavelet.
	 *
	 * @return a map of threads in this wavelet, that is keyed by thread id.
	 */
	public Map<String, BlipThread> getThreads();

	/**
	 * Returns the operation queue that this wavelet uses to queue operation to
	 * the robot proxy.
	 *
	 * @return an instance of {@link OperationQueue} that represents this
	 *     wavelet's operation queue.
	 */
	public OperationQueue getOperationQueue();

	/**
	 * Returns the domain of this wavelet.
	 *
	 * @return the wavelet domain, which is encoded in the wave/wavelet id.
	 */
	public String getDomain();

	/**
	 * Returns a view of this wavelet that will proxy for the specified id.
	 *
	 * A shallow copy of the current wavelet is returned with the
	 * {@code proxyingFor} field set. Any modifications made to this copy will be
	 * done using the {@code proxyForId}, i.e. the
	 * {@code robot+<proxyForId>@appspot.com} address will be used.
	 *
	 * If the wavelet was retrieved using the Active Robot API, that is
	 * by {@code fetchWavelet}, then the address of the robot must be added to the
	 * wavelet by calling {@code setRobotAddress} with the robot's address
	 * before calling {@code proxy_for}.
	 *
	 * @param proxyForId the id to proxy. Please note that this parameter should
	 *     be properly encoded to ensure that the resulting participant id is
	 *     valid (see {@link Util#checkIsValidProxyForId(String)} for more
	 *     details).
	 * @return a shallow copy of this wavelet with the proxying information set.
	 */
	public Wavelet proxyFor(String proxyForId);

	/**
	 * Submit this wavelet when the given {@code other} wavelet is submitted.
	 *
	 * Wavelets constructed outside of the event callback need to
	 * be either explicitly submitted using {@code AbstractRobot.submit(Wavelet)}
	 * or be associated with a different wavelet that will be submitted or is part
	 * of the event callback.
	 *
	 * @param other the other wavelet whose operation queue will be joined with.
	 */
	public void submitWith(Wavelet other);

	/**
	 * Replies to the conversation in this wavelet.
	 *
	 * @param initialContent the initial content of the reply.
	 * @return an instance of {@link Blip} that represents a transient version of
	 *     the reply.
	 *
	 * @throws IllegalArgumentException if {@code initialContent} does not start
	 *     with a newline character.
	 */
	public Blip reply(String initialContent);

	/**
	 * Removes a blip from this wavelet.
	 *
	 * @param blip the blip to be removed.
	 */
	public void delete(Blip blip);

	/**
	 * Removes a blip from this wavelet.
	 *
	 * @param blipId the id of the blip to be removed.
	 */
	public void delete(String blipId);

	/**
	 * Serializes this {@link Wavelet} into a {@link WaveletData}.
	 *
	 * @return an instance of {@link WaveletData} that represents this wavelet.
	 */
	public WaveletData serialize();

}