/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.google.wave.api.impl;

import com.google.wave.api.BlipData;
import com.google.wave.api.Context;
import com.google.wave.api.BlipThread;
import com.google.wave.api.Wavelet;
import com.google.wave.api.event.Event;
import com.google.wave.api.event.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A container for a bundle of messages to be sent to a robot.
 *
 * @author scovitz@google.com (Seth Covitz)
 */
public class EventMessageBundleImpl implements EventMessageBundle {

  private List<Event> events;
  private WaveletData waveletData;
  private Wavelet wavelet;
  private Map<String, BlipData> blipData;
  private Map<String, BlipThread> threads;
  private Map<String, Set<Context>> requiredBlips;
  private String proxyingFor;
  private final String robotAddress;
  private final String rpcServerUrl;

  public EventMessageBundleImpl(String robotAddress, String rpcServerUrl) {
    this.robotAddress = robotAddress;
    events = new ArrayList<Event>();
    this.rpcServerUrl = rpcServerUrl;
    blipData = new HashMap<String, BlipData>();
    requiredBlips = new HashMap<String, Set<Context>>();
    threads = new HashMap<String, BlipThread>();
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getRequiredBlips()
 */
@Override
public Map<String, Set<Context>> getRequiredBlips() {
    return requiredBlips;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#requireBlip(java.lang.String, java.util.List)
 */
  @Override
public void requireBlip(String blipId, List<Context> contexts) {
    Set<Context> contextSet = requiredBlips.get(blipId);
    if (contextSet == null) {
      contextSet = new HashSet<Context>();
      requiredBlips.put(blipId, contextSet);
    }
    for (Context context : contexts) {
      contextSet.add(context);
    }
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#addEvent(com.google.wave.api.event.Event)
 */
  @Override
public void addEvent(Event event) {
    events.add(event);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#hasMessages()
 */
@Override
public boolean hasMessages() {
    return !events.isEmpty();
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getEvents()
 */
@Override
public List<Event> getEvents() {
    return events;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getWaveletData()
 */
@Override
public WaveletData getWaveletData() {
    return waveletData;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getWavelet()
 */
@Override
public Wavelet getWavelet() {
    return wavelet;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getBlipData()
 */
@Override
public Map<String, BlipData> getBlipData() {
    return blipData;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getThreads()
 */
@Override
public Map<String, BlipThread> getThreads() {
    return threads;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#setEvents(java.util.List)
 */
@Override
public void setEvents(List<Event> events) {
    this.events = events;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#setWaveletData(com.google.wave.api.impl.WaveletData)
 */
@Override
public void setWaveletData(WaveletData waveletData) {
    this.waveletData = waveletData;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#setWavelet(com.google.wave.api.Wavelet)
 */
@Override
public void setWavelet(Wavelet wavelet) {
    this.wavelet = wavelet;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#setBlipData(java.util.Map)
 */
@Override
public void setBlipData(Map<String, BlipData> blipData) {
    this.blipData = blipData;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#setThreads(java.util.Map)
 */
@Override
public void setThreads(Map<String, BlipThread> threads) {
    this.threads = threads;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#clear()
 */
@Override
public void clear() {
    events.clear();
    blipData.clear();
    requiredBlips.clear();
    waveletData = null;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#hasBlipId(java.lang.String)
 */
  @Override
public boolean hasBlipId(String id) {
    return blipData.containsKey(id);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#addBlip(java.lang.String, com.google.wave.api.BlipData)
 */
  @Override
public void addBlip(String id, BlipData blip) {
    blipData.put(id, blip);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#addThread(java.lang.String, com.google.wave.api.BlipThread)
 */
  @Override
public void addThread(String id, BlipThread thread) {
    threads.put(id, thread);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#hasThreadId(java.lang.String)
 */
  @Override
public boolean hasThreadId(String id) {
    return threads.containsKey(id);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#hasEvent(com.google.wave.api.event.EventType)
 */
  @Override
public boolean hasEvent(EventType eventType) {
    for (Event event : events) {
      if (event.getType() == eventType) {
        return true;
      }
    }
    return false;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getProxyingFor()
 */
@Override
public String getProxyingFor() {
    return proxyingFor;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#setProxyingFor(java.lang.String)
 */
@Override
public void setProxyingFor(String proxyingFor) {
    this.proxyingFor = proxyingFor;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getRobotAddress()
 */
@Override
public String getRobotAddress() {
    return robotAddress;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.impl.EventMessageBundle#getRpcServerUrl()
 */
@Override
public String getRpcServerUrl() {
    return rpcServerUrl;
  }
}
