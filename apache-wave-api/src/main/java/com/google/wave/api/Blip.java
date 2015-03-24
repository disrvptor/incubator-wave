package com.google.wave.api;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;

public interface Blip {

	/**
	 * Returns the id of this blip.
	 *
	 * @return the blip id.
	 */
	public String getBlipId();

	/**
	 * Returns the id of the wave that owns this blip.
	 *
	 * @return the wave id.
	 */
	public WaveId getWaveId();

	/**
	 * Returns the id of the wavelet that owns this blip.
	 *
	 * @return the wavelet id.
	 */
	public WaveletId getWaveletId();

	/**
	 * Returns the list of ids of this blip children.
	 *
	 * @return the children's ids.
	 */
	public List<String> getChildBlipIds();

	/**
	 * Returns the list of child blips.
	 *
	 * @return the children of this blip.
	 */
	public List<Blip> getChildBlips();

	/**
	 * @return the inline reply threads of this blip, sorted by the offset.
	 */
	public Collection<BlipThread> getInlineReplyThreads();

	/**
	 * @return the reply threads of this blip.
	 */
	public Collection<BlipThread> getReplyThreads();

	/**
	 * Returns the participant ids of the contributors of this blip.
	 *
	 * @return the blip's contributors.
	 */
	public List<String> getContributors();

	/**
	 * Returns the participant id of the creator of this blip.
	 *
	 * @return the blip's creator.
	 */
	public String getCreator();

	/**
	 * Returns the last modified time of this blip.
	 *
	 * @return the blip's last modified time.
	 */
	public long getLastModifiedTime();

	/**
	 * Returns the version of this blip.
	 *
	 * @return the blip's version.
	 */
	public long getVersion();

	/**
	 * Returns the id of this blip's parent, or {@code null} if this blip is in
	 * the root thread.
	 *
	 * @return the blip's parent's id.
	 */
	public String getParentBlipId();

	/**
	 * Returns the parent blip.
	 *
	 * @return the parent of this blip.
	 */
	public Blip getParentBlip();

	/**
	 * @return the containing thread.
	 */
	public BlipThread getThread();

	/**
	 * Checks whether this is a root blip or not.
	 *
	 * @return {@code true} if this is a root blip, denoted by {@code null} parent
	 *     id.
	 */
	public boolean isRoot();

	/**
	 * Returns the annotations for this blip's content.
	 *
	 * @return the blip's annotations.
	 */
	public Annotations getAnnotations();

	/**
	 * Returns the elements content of this blip.
	 *
	 * @return the blip's elements.
	 */
	public SortedMap<Integer, Element> getElements();

	/**
	 * Returns the text content of this blip.
	 *
	 * @return blip's content.
	 */
	public String getContent();

	/**
	 * Sets the content of this blip.
	 *
	 * @param content the blip's content.
	 */
	public void setContent(String content);

	/**
	 * Returns the length/size of the blip, denoted by the length of this blip's
	 * text content.
	 *
	 * @return the size of the blip.
	 */
	public int length();

	/**
	 * Returns the wavelet that owns this Blip.
	 *
	 * @return the wavelet.
	 */
	public Wavelet getWavelet();

	/**
	 * Returns a reference to the entire content of the blip.
	 *
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs all();

	/**
	 * Returns all references to this blip's content that match {@code target}.
	 *
	 * @param target the text to search for.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs all(String target);

	/**
	 * Returns all references to this blip's content that match {@code target}.
	 * This blip references object will have at most {@code maxResult} hits.
	 *
	 * @param target the text to search for.
	 * @param maxResult the maximum number of hits. Specify -1 for no limit.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs all(String target, int maxResult);

	/**
	 * Returns all references to this blip's content that match {@code target} and
	 * {@code restrictions}.
	 *
	 * @param target the element type to search for.
	 * @param restrictions the element properties that need to be matched.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs all(ElementType target, Restriction... restrictions);

	/**
	 * Returns all references to this blip's content that match {@code target} and
	 * {@code restrictions}. This blip references object will have at most
	 * {@code maxResult} hits.
	 *
	 * @param target the element type to search for.
	 * @param maxResult the maximum number of hits. Specify -1 for no limit.
	 * @param restrictions the element properties that need to be matched.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs all(ElementType target, int maxResult,
			Restriction... restrictions);

	/**
	 * Returns the first reference to this blip's content that matches
	 * {@code target}.
	 *
	 * @param target the text to search for.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs first(String target);

	/**
	 * Returns the first reference to this blip's content that matches
	 * {@code target} and {@code restrictions}.
	 *
	 * @param target the type of element to search for.
	 * @param restrictions the list of restrictions to filter the search.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs first(ElementType target,
			Restriction... restrictions);

	/**
	 * Returns the reference to this blip's content at the specified index.
	 *
	 * @param index the index to reference.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs at(int index);

	/**
	 * Returns the reference to this blip's content at the specified range.
	 *
	 * @param start the start index of the range to reference.
	 * @param end the end index of the range to reference.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs range(int start, int end);

	/**
	 * Appends the given argument (element, text, or markup) to the blip.
	 *
	 * @param argument the element, text, or markup to be appended.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs append(BlipContent argument);

	/**
	 * Appends the given string to the blip.
	 *
	 * @param argument the string to be appended.
	 * @return an instance of {@link BlipContentRefs}.
	 */
	public BlipContentRefs append(String argument);

	/**
	 * Creates a reply to this blip.
	 *
	 * @return an instance of {@link Blip} that represents a reply to the blip.
	 */
	public Blip reply();

	/**
	 * Continues the containing thread of this blip..
	 *
	 * @return an instance of {@link Blip} that represents a the new continuation
	 *     reply blip.
	 */
	public Blip continueThread();

	/**
	 * Inserts an inline blip at the given position.
	 *
	 * @param position the index to insert the inline blip at. This has to be
	 *     greater than 0.
	 * @return an instance of {@link Blip} that represents the new inline blip.
	 */
	public Blip insertInlineBlip(int position);

	/**
	 * Appends markup ({@code HTML}) content.
	 *
	 * @param markup the markup content to add.
	 */
	public void appendMarkup(String markup);

	/**
	 * Returns a view of this blip that will proxy for the specified id.
	 *
	 * A shallow copy of the current blip is returned with the {@code proxyingFor}
	 * field set. Any modifications made to this copy will be done using the
	 * {@code proxyForId}, i.e. the {@code robot+<proxyForId>@appspot.com} address
	 * will be used.
	 *
	 * @param proxyForId the id to proxy. Please note that this parameter should
	 *     be properly encoded to ensure that the resulting participant id is
	 *     valid (see {@link Util#checkIsValidProxyForId(String)} for more
	 *     details).
	 * @return a shallow copy of this blip with the proxying information set.
	 */
	public Blip proxyFor(String proxyForId);

	/**
	 * Returns the offset of this blip if it is inline, or -1 if it's not. If the
	 * parent is not in the offset, this method will always return -1 since it
	 * can't determine the inline blip status.
	 *
	 * @return the offset of this blip if it is inline, or -1 if it's not inline
	 *     or if the parent is not in the context.
	 * @deprecated please use {@code getThread().getLocation()} to get the offset
	 *     of the inline reply thread that contains this blip.
	 */
	public int getInlineBlipOffset();

	/**
	 * Deletes the given blip id from the list of child blip ids.
	 *
	 * @param childBlipId the blip id to delete.
	 */
	public void deleteChildBlipId(String childBlipId);

	/**
	 * Adds the given {@link BlipThread} as a reply or inline reply thread.
	 *
	 * @param thread the new thread to add.
	 */
	public void addThread(BlipThread thread);

	/**
	 * Removes the given {@link BlipThread} from the reply or inline reply thread.
	 *
	 * @param thread the new thread to remove.
	 */
	public void removeThread(BlipThread thread);

	/**
	 * Serializes this {@link Blip} into a {@link BlipData}.
	 *
	 * @return an instance of {@link BlipData} that represents this blip.
	 */
	public BlipData serialize();

}