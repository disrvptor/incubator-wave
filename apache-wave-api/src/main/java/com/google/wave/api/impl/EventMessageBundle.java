package com.google.wave.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.wave.api.BlipData;
import com.google.wave.api.BlipThread;
import com.google.wave.api.Context;
import com.google.wave.api.Wavelet;
import com.google.wave.api.event.Event;
import com.google.wave.api.event.EventType;

public interface EventMessageBundle {

	public Map<String, Set<Context>> getRequiredBlips();

	/**
	 * Require the availability of the specified blipId for this bundle.
	 *
	 * @param blipId the id of the blip that is required.
	 * @param contexts we need for this blip.
	 */
	public void requireBlip(String blipId, List<Context> contexts);

	/**
	 * Add an event to the events that are tracked.
	 * @param event to add.
	 */
	public void addEvent(Event event);

	public boolean hasMessages();

	public List<Event> getEvents();

	public WaveletData getWaveletData();

	public Wavelet getWavelet();

	public Map<String, BlipData> getBlipData();

	public Map<String, BlipThread> getThreads();

	public void setEvents(List<Event> events);

	public void setWaveletData(WaveletData waveletData);

	public void setWavelet(Wavelet wavelet);

	public void setBlipData(Map<String, BlipData> blipData);

	public void setThreads(Map<String, BlipThread> threads);

	public void clear();

	/**
	 * Return whether a blip is already in the blipdata
	 *
	 * @param id of the blip
	 * @return whether it is in blipData
	 */
	public boolean hasBlipId(String id);

	/**
	 * Add a blip to the blipdata
	 *
	 * @param id
	 * @param blip
	 */
	public void addBlip(String id, BlipData blip);

	/**
	 * Add a thread to the map of threads.
	 *
	 * @param id the thread id.
	 * @param thread the thread to add.
	 */
	public void addThread(String id, BlipThread thread);

	/**
	 * Check whether a given thread has been added to the thread map or not.
	 *
	 * @param id the id of the thread to check.
	 * @return {@code true} if the map contains the given thread id.
	 */
	public boolean hasThreadId(String id);

	/**
	 * Return whether the lookingFor event is contained in this bundle.
	 */
	public boolean hasEvent(EventType eventType);

	public String getProxyingFor();

	public void setProxyingFor(String proxyingFor);

	public String getRobotAddress();

	public String getRpcServerUrl();

}