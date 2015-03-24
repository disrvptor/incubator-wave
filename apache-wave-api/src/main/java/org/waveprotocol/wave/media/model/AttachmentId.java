package org.waveprotocol.wave.media.model;

public interface AttachmentId {

	/** Separates the domain from the id in an attachment id. */
	public static final String ATTACHMENT_PART_SEPARATOR = "/";

	/**
	 * @return the domain
	 */
	public String getDomain();

	/**
	 * @return the id
	 */
	public String getId();

	/**
	 * Serialises this attachmentId into a unique string. For any two attachment
	 * ids, attachmentId1.serialise().equals(attachmentId2.serialise()) iff
	 * attachmentId1.equals(attachmentId2).
	 */
	public String serialise();

	public int compareTo(AttachmentId other);

}