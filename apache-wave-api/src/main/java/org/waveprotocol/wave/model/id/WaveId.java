package org.waveprotocol.wave.model.id;

public interface WaveId {

	/**
	 * @return the domain
	 */
	public String getDomain();

	/**
	 * @return the local id
	 */
	public String getId();

	/**
	 * Serialises this waveId into a unique string. For any two wave ids,
	 * waveId1.serialise().equals(waveId2.serialise()) iff waveId1.equals(waveId2).
	 */
	public String serialise();

	public int compareTo(WaveId other);

}