package org.waveprotocol.wave.model.id;

public interface WaveletId {

	/**
	 * @return the domain
	 */
	public String getDomain();

	/**
	 * @return the local id
	 */
	public String getId();

	/**
	 * Serialises this waveletId into a unique string. For any two wavelet ids,
	 * waveletId1.serialise().equals(waveletId2.serialise()) iff waveId1.equals(waveId2).
	 */
	public String serialise();

	public int compareTo(WaveletId other);

}