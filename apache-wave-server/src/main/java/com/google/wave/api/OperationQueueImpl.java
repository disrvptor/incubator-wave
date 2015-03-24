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

package com.google.wave.api;

import com.google.wave.api.JsonRpcConstant.ParamsProperty;
import com.google.wave.api.OperationRequest.Parameter;
import com.google.wave.api.impl.RawAttachmentData;

import org.waveprotocol.wave.model.id.InvalidIdException;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.media.model.AttachmentId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * A utility class that abstracts the queuing of operations, represented by
 * {@link OperationRequest}, using easily callable functions. The
 * {@link OperationQueue} queues the resulting operations in order.
 *
 * Typically there shouldn't be a need to call this directly unless operations
 * are needed on entities outside of the scope of the robot. For example, to
 * modify a blip that does not exist in the current context, you might specify
 * the wave, wavelet, and blip id to generate an operation.
 *
 * Any calls to this will not be reflected in the robot in any way. For example,
 * calling wavelet_append_blip will not result in a new blip being added to the
 * robot current context, only an operation to be sent to the robot proxy.
 */
public class OperationQueueImpl implements Serializable, OperationQueue {

  /** A random number generator for the temporary ids. */
  private static final Random ID_GENERATOR = new Random();

  /** The format of temporary blip ids. */
  private static final String TEMP_BLIP_ID_FORMAT = "TBD_%s_%s";

  /** The format of temporary wave ids. */
  private static final String TEMP_WAVE_ID_FORMAT = "%s!TBD_%s";

  /** The format of wavelet ids. */
  private static final String TEMP_WAVELET_ID_FORMAT = "%s!conv+root";

  /** The format of new operation ids. */
  private static final String OP_ID_FORMAT = "op%d";

  /** Some class global counters. */
  private static long nextOpId = 1;

  /** The id that can be set for {@code proxyingFor} parameter. */
  private final String proxyForId;

  /** The operation queue. */
  private List<OperationRequest> pendingOperations;

  /**
   * Constructor that creates a new instance of {@link OperationQueue} with
   * an empty queue and no proxying information set.
   */
  public OperationQueueImpl() {
    this(new ArrayList<OperationRequest>(), null);
  }

  /**
   * Constructor that creates a new instance of {@link OperationQueue} with
   * a specified proxying information.
   *
   * @param proxyForId the proxying information.
   */
  public OperationQueueImpl(String proxyForId) {
    this(new ArrayList<OperationRequest>(), proxyForId);
  }

  /**
   * Constructor that creates a new instance of {@link OperationQueue} with
   * a specified queue and proxying information.
   *
   * @param operations the underlying operation queue that should be used for
   *     this {@link OperationQueue}.
   * @param proxyForId the proxying information.
   */
  public OperationQueueImpl(List<OperationRequest> operations, String proxyForId) {
    this.pendingOperations = operations;
    this.proxyForId = proxyForId;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#getPendingOperations()
 */
  @Override
public List<OperationRequest> getPendingOperations() {
    return pendingOperations;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#getProxyForId()
 */
  @Override
public String getProxyForId() {
    return proxyForId;
  }

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
  @Override
public OperationQueue proxyFor(String proxyForId) {
    return new OperationQueueImpl(pendingOperations, proxyForId);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#clear()
 */
  @Override
public void clear() {
    pendingOperations.clear();
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#appendBlipToWavelet(com.google.wave.api.Wavelet, java.lang.String)
 */
  @Override
public Blip appendBlipToWavelet(Wavelet wavelet, String initialContent) {
    Blip newBlip = newBlip(wavelet, initialContent, null, generateTempBlipId(wavelet),
        wavelet.getRootThread().getId());
    appendOperation(OperationType.WAVELET_APPEND_BLIP, wavelet,
        Parameter.of(ParamsProperty.BLIP_DATA, newBlip.serialize()));
    return newBlip;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#addParticipantToWavelet(com.google.wave.api.Wavelet, java.lang.String)
 */
  @Override
public void addParticipantToWavelet(Wavelet wavelet, String participantId) {
    appendOperation(OperationType.WAVELET_ADD_PARTICIPANT_NEWSYNTAX, wavelet,
        Parameter.of(ParamsProperty.PARTICIPANT_ID, participantId));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#removeParticipantFromWavelet(com.google.wave.api.Wavelet, java.lang.String)
 */
  @Override
public void removeParticipantFromWavelet(Wavelet wavelet, String participantId) {
    appendOperation(OperationType.WAVELET_REMOVE_PARTICIPANT_NEWSYNTAX, wavelet,
        Parameter.of(ParamsProperty.PARTICIPANT_ID, participantId));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#createWavelet(java.lang.String, java.util.Set)
 */
  @Override
public Wavelet createWavelet(String domain, Set<String> participants) {
    return createWavelet(domain, participants, "");
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#createWavelet(java.lang.String, java.util.Set, java.lang.String)
 */
  @Override
public Wavelet createWavelet(String domain, Set<String> participants, String message) {
    Wavelet newWavelet = newWavelet(domain, participants, this);
    OperationRequest operation = appendOperation(OperationType.ROBOT_CREATE_WAVELET,
        newWavelet, Parameter.of(ParamsProperty.WAVELET_DATA, newWavelet.serialize()));

    // Don't add the message if it's null or empty.
    if (message != null && !message.isEmpty()) {
      operation.addParameter(Parameter.of(ParamsProperty.MESSAGE, message));
    }
    return newWavelet;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#search(java.lang.String, java.lang.Integer, java.lang.Integer)
 */
  @Override
public void search(String query, Integer index, Integer numresults) {
    Parameter queryParam = Parameter.of(ParamsProperty.QUERY, query);
    Parameter indexParam = Parameter.of(ParamsProperty.INDEX, index);
    Parameter numresultsParam = Parameter.of(ParamsProperty.NUM_RESULTS, numresults);
    appendOperation(OperationType.ROBOT_SEARCH, queryParam, indexParam, numresultsParam);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#setDatadocOfWavelet(com.google.wave.api.Wavelet, java.lang.String, java.lang.String)
 */
  @Override
public void setDatadocOfWavelet(Wavelet wavelet, String name, String value) {
    appendOperation(OperationType.WAVELET_SET_DATADOC, wavelet,
        Parameter.of(ParamsProperty.DATADOC_NAME, name),
        Parameter.of(ParamsProperty.DATADOC_VALUE, value));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#fetchWavelet(org.waveprotocol.wave.model.id.WaveId, org.waveprotocol.wave.model.id.WaveletId)
 */
  @Override
public void fetchWavelet(WaveId waveId, WaveletId waveletId) {
    appendOperation(OperationType.ROBOT_FETCH_WAVE, waveId, waveletId, null);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#retrieveWaveletIds(org.waveprotocol.wave.model.id.WaveId)
 */
  @Override
public void retrieveWaveletIds(WaveId waveId) {
    appendOperation(OperationType.ROBOT_FETCH_WAVE, waveId, null, null,
        Parameter.of(ParamsProperty.RETURN_WAVELET_IDS, true));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#exportSnapshot(org.waveprotocol.wave.model.id.WaveId, org.waveprotocol.wave.model.id.WaveletId)
 */
  @Override
public void exportSnapshot(WaveId waveId, WaveletId waveletId) {
    appendOperation(OperationType.ROBOT_EXPORT_SNAPSHOT, waveId, waveletId, null);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#exportRawDeltas(org.waveprotocol.wave.model.id.WaveId, org.waveprotocol.wave.model.id.WaveletId, byte[], byte[])
 */
  @Override
public void exportRawDeltas(WaveId waveId, WaveletId waveletId,
      byte[] fromVersion, byte[] toVersion) {
    appendOperation(OperationType.ROBOT_EXPORT_DELTAS, waveId, waveletId, null,
        Parameter.of(ParamsProperty.FROM_VERSION, fromVersion),
        Parameter.of(ParamsProperty.TO_VERSION, toVersion));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#exportAttachment(org.waveprotocol.wave.media.model.AttachmentId)
 */
  @Override
public void exportAttachment(AttachmentId attachmentId) {
    appendOperation(OperationType.ROBOT_EXPORT_ATTACHMENT,
        Parameter.of(ParamsProperty.ATTACHMENT_ID, attachmentId.serialise()));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#importRawDeltas(org.waveprotocol.wave.model.id.WaveId, org.waveprotocol.wave.model.id.WaveletId, java.util.List)
 */
  @Override
public void importRawDeltas(WaveId waveId, WaveletId waveletId, List<byte[]> history) {
    appendOperation(OperationType.ROBOT_IMPORT_DELTAS,
        waveId, waveletId, null, Parameter.of(ParamsProperty.RAW_DELTAS, history));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#importAttachment(org.waveprotocol.wave.model.id.WaveId, org.waveprotocol.wave.model.id.WaveletId, org.waveprotocol.wave.media.model.AttachmentId, com.google.wave.api.impl.RawAttachmentData)
 */
  @Override
public void importAttachment(WaveId waveId, WaveletId waveletId,
      AttachmentId attachmentId, RawAttachmentData attachmentData) {
    appendOperation(OperationType.ROBOT_IMPORT_ATTACHMENT,
        waveId, waveletId, null,
        Parameter.of(ParamsProperty.ATTACHMENT_ID, attachmentId.serialise()),
        Parameter.of(ParamsProperty.ATTACHMENT_DATA, attachmentData));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#setTitleOfWavelet(com.google.wave.api.Wavelet, java.lang.String)
 */
  @Override
public void setTitleOfWavelet(Wavelet wavelet, String title) {
    appendOperation(OperationType.WAVELET_SET_TITLE, wavelet,
        Parameter.of(ParamsProperty.WAVELET_TITLE, title));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#modifyTagOfWavelet(com.google.wave.api.Wavelet, java.lang.String, java.lang.String)
 */
  @Override
public void modifyTagOfWavelet(Wavelet wavelet, String tag, String modifyHow) {
    appendOperation(OperationType.WAVELET_MODIFY_TAG, wavelet,
        Parameter.of(ParamsProperty.NAME, tag),
        Parameter.of(ParamsProperty.MODIFY_HOW, modifyHow));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#createChildOfBlip(com.google.wave.api.Blip)
 */
  @Override
public Blip createChildOfBlip(Blip blip) {
    // Create a new thread.
    String tempBlipId = generateTempBlipId(blip.getWavelet());
    Wavelet wavelet = blip.getWavelet();
    BlipThread thread = new BlipThread(tempBlipId, -1, new ArrayList<String>(),
        wavelet.getBlips());

    // Add the new thread to the blip and wavelet.
    blip.addThread(thread);
    ((WaveletImpl)wavelet).addThread(thread);

    // Create a new blip in the new thread.
    Blip newBlip = newBlip(blip.getWavelet(), "", blip.getBlipId(), tempBlipId, thread.getId());
    appendOperation(OperationType.BLIP_CREATE_CHILD, blip,
        Parameter.of(ParamsProperty.BLIP_DATA, newBlip.serialize()));
    return newBlip;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#continueThreadOfBlip(com.google.wave.api.Blip)
 */
  @Override
public Blip continueThreadOfBlip(Blip blip) {
    Blip newBlip = newBlip(blip.getWavelet(), "", blip.getParentBlipId(),
        generateTempBlipId(blip.getWavelet()), blip.getThread().getId());
    appendOperation(OperationType.BLIP_CONTINUE_THREAD, blip,
        Parameter.of(ParamsProperty.BLIP_DATA, newBlip.serialize()));
    return newBlip;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#deleteBlip(com.google.wave.api.Wavelet, java.lang.String)
 */
  @Override
public void deleteBlip(Wavelet wavelet, String blipId) {
    appendOperation(OperationType.BLIP_DELETE, wavelet.getWaveId(), wavelet.getWaveletId(),
        blipId);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#appendMarkupToDocument(com.google.wave.api.Blip, java.lang.String)
 */
  @Override
public void appendMarkupToDocument(Blip blip, String content) {
    appendOperation(OperationType.DOCUMENT_APPEND_MARKUP, blip,
        Parameter.of(ParamsProperty.CONTENT, content));
  }

  /**
   * Submits this operation queue when the given {@code other} operation queue
   * is submitted.
   *
   * @param other the other operation queue to merge this operation queue with.
   */
  @Override
public void submitWith(OperationQueue other) {
    other.getPendingOperations().addAll(this.pendingOperations);
    this.pendingOperations = new ArrayList<>(other.getPendingOperations());
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#modifyDocument(com.google.wave.api.Blip)
 */
  @Override
public OperationRequest modifyDocument(Blip blip) {
    return appendOperation(OperationType.DOCUMENT_MODIFY, blip);
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#insertInlineBlipToDocument(com.google.wave.api.Blip, int)
 */
  @Override
public Blip insertInlineBlipToDocument(Blip blip, int position) {
    // Create a new thread.
    String tempBlipId = generateTempBlipId(blip.getWavelet());
    Wavelet wavelet = blip.getWavelet();
    BlipThread thread = new BlipThread(tempBlipId, position, new ArrayList<String>(),
        wavelet.getBlips());

    // Add the new thread to the blip and wavelet.
    blip.addThread(thread);
    ((WaveletImpl)wavelet).addThread(thread);

    // Create a new blip in the new thread.
    Blip inlineBlip = newBlip(blip.getWavelet(), "", blip.getBlipId(), tempBlipId,
        thread.getId());
    appendOperation(OperationType.DOCUMENT_INSERT_INLINE_BLIP, blip,
        Parameter.of(ParamsProperty.INDEX, position),
        Parameter.of(ParamsProperty.BLIP_DATA, inlineBlip.serialize()));
    return inlineBlip;
  }

  /**
   * Creates and appends a new operation to the operation queue.
   *
   * @param opType the type of the operation.
   * @param parameters the parameters that should be added as a property of
   *     the operation.
   * @return an instance of {@link OperationRequest} that represents the queued
   *     operation.
   */
  OperationRequest appendOperation(OperationType opType, Parameter... parameters) {
    return appendOperation(opType, null, null, null, parameters);
  }

  /**
   * Creates and appends a new operation to the operation queue.
   *
   * @param opType the type of the operation.
   * @param wavelet the wavelet to apply the operation to.
   * @param parameters the parameters that should be added as a property of
   *     the operation.
   * @return an instance of {@link OperationRequest} that represents the queued
   *     operation.
   */
  OperationRequest appendOperation(OperationType opType, Wavelet wavelet,
      Parameter... parameters) {
    return appendOperation(opType, wavelet.getWaveId(), wavelet.getWaveletId(), null, parameters);
  }

  /**
   * Creates and appends a new operation to the operation queue.
   *
   * @param opType the type of the operation.
   * @param blip the blip to apply this operation to.
   * @param parameters the parameters that should be added as a property of
   *     the operation.
   * @return an instance of {@link OperationRequest} that represents the queued
   *     operation.
   */
  OperationRequest appendOperation(OperationType opType, Blip blip, Parameter... parameters) {
    return appendOperation(opType, blip.getWaveId(), blip.getWaveletId(), blip.getBlipId(),
        parameters);
  }

  /**
   * Creates and appends a new operation to the operation queue.
   *
   * @param opType the type of the operation.
   * @param waveId the wave id in which the operation should be applied to.
   * @param waveletId the wavelet id of the given wave in which the operation
   *     should be applied to.
   * @param blipId the optional blip id of the given wave in which the operation
   *     should be applied to. Not all operations require blip id.
   * @param parameters the parameters that should be added as a property of
   *     the operation.
   * @return an instance of {@link OperationRequest} that represents the queued
   *     operation.
   */
  OperationRequest appendOperation(OperationType opType, WaveId waveId, WaveletId waveletId,
      String blipId, Parameter... parameters) {
    return addOperation(opType, waveId, waveletId, blipId, pendingOperations.size(), parameters);
  }

  /**
   * Creates and prepends a new operation to the operation queue.
   *
   * @param opType the type of the operation.
   * @param waveId the wave id in which the operation should be applied to.
   * @param waveletId the wavelet id of the given wave in which the operation
   *     should be applied to.
   * @param blipId the optional blip id of the given wave in which the operation
   *     should be applied to. Not all operations require blip id.
   * @param parameters the parameters that should be added as a property of
   *     the operation.
   * @return an instance of {@link OperationRequest} that represents the queued
   *     operation.
   */
  OperationRequest prependOperation(OperationType opType, WaveId waveId, WaveletId waveletId,
      String blipId, Parameter... parameters) {
    return addOperation(opType, waveId, waveletId, blipId, 0, parameters);
  }

  /**
   * Creates and adds a new operation to the operation queue.
   *
   * @param opType the type of the operation.
   * @param waveId the wave id in which the operation should be applied to.
   * @param waveletId the wavelet id of the given wave in which the operation
   *     should be applied to.
   * @param blipId the optional blip id of the given wave in which the operation
   *     should be applied to. Not all operations require blip id.
   * @param index the index where this new operation should be added to in the
   *     queue.
   * @param parameters the parameters that should be added as a property of
   *     the operation.
   * @return an instance of {@link OperationRequest} that represents the queued
   *     operation.
   */
  OperationRequest addOperation(OperationType opType, WaveId waveId, WaveletId waveletId,
      String blipId, int index, Parameter... parameters) {
    String waveIdString = null;
    if (waveId != null) {
      waveIdString = ApiIdSerializer.instance().serialiseWaveId(waveId);
    }

    String waveletIdString = null;
    if (waveletId != null) {
      waveletIdString = ApiIdSerializer.instance().serialiseWaveletId(waveletId);
    }

    OperationRequest operation = new OperationRequest(opType.method(),
        String.format(OP_ID_FORMAT, nextOpId++),
        waveIdString, waveletIdString, blipId, parameters);

    // Set the proxying for parameter, if necessary.
    if (proxyForId != null && !proxyForId.isEmpty()) {
      operation.addParameter(Parameter.of(ParamsProperty.PROXYING_FOR, proxyForId));
    }

    pendingOperations.add(index, operation);
    return operation;
  }

  /**
   * Generates a temporary blip id.
   *
   * @param wavelet the wavelet to seed the temporary id.
   * @return a temporary blip id.
   */
  private static String generateTempBlipId(Wavelet wavelet) {
    return String.format(TEMP_BLIP_ID_FORMAT,
        ApiIdSerializer.instance().serialiseWaveletId(wavelet.getWaveletId()),
        ID_GENERATOR.nextInt());
  }

  /**
   * Creates a new {@code Blip} object used for this session. A temporary
   * id will be assigned to the newly created {@code Blip} object.
   *
   * @param wavelet the wavelet that owns this blip.
   * @param initialContent the initial content of the new blip.
   * @param parentBlipId the parent of this blip.
   * @return an instance of new {@code Blip} object used for this session.
   */
  private static Blip newBlip(Wavelet wavelet, String initialContent, String parentBlipId,
      String blipId, String threadId) {
    Blip newBlip = new BlipImpl(blipId, initialContent, parentBlipId, threadId, wavelet);
    if (parentBlipId != null) {
      Blip parentBlip = wavelet.getBlips().get(parentBlipId);
      if (parentBlip != null) {
        parentBlip.getChildBlipIds().add(newBlip.getBlipId());
      }
    }
    wavelet.getBlips().put(newBlip.getBlipId(), newBlip);

    BlipThread thread = wavelet.getThread(threadId);
    if (thread != null) {
      thread.appendBlip(newBlip);
    }
    return newBlip;
  }

  /**
   * Creates a new {@code Wavelet} object used for this session. A temporary
   * wave id will be assigned to this newly created {@code Wavelet} object.
   *
   * @param domain the domain that is used for the wave and wavelet ids.
   * @param participants the participants that should be added to the new
   *     wavelet.
   * @param opQueue the operation queue of the new wavelet.
   * @return an instance of new {@code Wavelet} object used for this
   *     session.
   */
  private static Wavelet newWavelet(String domain, Set<String> participants,
      OperationQueue opQueue) {
    // Make sure that participant list is not null;
    if (participants == null) {
      participants = Collections.emptySet();
    }

    WaveId waveId;
    WaveletId waveletId;
    try {
      waveId = ApiIdSerializer.instance().deserialiseWaveId(
          String.format(TEMP_WAVE_ID_FORMAT, domain, ID_GENERATOR.nextInt()));
      waveletId = ApiIdSerializer.instance().deserialiseWaveletId(
          String.format(TEMP_WAVELET_ID_FORMAT, domain));
    } catch (InvalidIdException e) {
      throw new IllegalStateException("Invalid temporary id", e);
    }

    String rootBlipId = String.format(TEMP_BLIP_ID_FORMAT,
        ApiIdSerializer.instance().serialiseWaveletId(waveletId),
        ID_GENERATOR.nextInt());
    Map<String, Blip> blips = new HashMap<String, Blip>();
    Map<String, String> roles = new HashMap<String, String>();
    Map<String, BlipThread> threads = new HashMap<String, BlipThread>();

    List<String> blipIds = new ArrayList<String>();
    blipIds.add(rootBlipId);
    BlipThread rootThread = new BlipThread("", -1, blipIds, blips);

    Wavelet wavelet = new WaveletImpl(waveId, waveletId, rootBlipId, rootThread, participants,
        roles, blips, threads, opQueue);

    Blip rootBlip = new BlipImpl(rootBlipId, "", null, "", wavelet);
    blips.put(rootBlipId, rootBlip);

    return wavelet;
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#modifyParticipantRoleOfWavelet(com.google.wave.api.Wavelet, java.lang.String, java.lang.String)
 */
  @Override
public void modifyParticipantRoleOfWavelet(Wavelet wavelet, String participant, String role) {
    appendOperation(OperationType.WAVELET_MODIFY_PARTICIPANT_ROLE, wavelet,
        Parameter.of(ParamsProperty.PARTICIPANT_ID, participant),
        Parameter.of(ParamsProperty.PARTICIPANT_ROLE, role));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#notifyRobotInformation(com.google.wave.api.ProtocolVersion, java.lang.String)
 */
  @Override
public void notifyRobotInformation(ProtocolVersion protocolVersion, String capabilitiesHash) {
    prependOperation(OperationType.ROBOT_NOTIFY, null, null, null,
        Parameter.of(ParamsProperty.PROTOCOL_VERSION, protocolVersion.getVersionString()),
        Parameter.of(ParamsProperty.CAPABILITIES_HASH, capabilitiesHash));
  }

  /* (non-Javadoc)
 * @see com.google.wave.api.OperationQueue#fetchProfiles(com.google.wave.api.FetchProfilesRequest)
 */
@Override
public void fetchProfiles(FetchProfilesRequest request) {
    appendOperation(OperationType.ROBOT_FETCH_PROFILES,
        Parameter.of(ParamsProperty.FETCH_PROFILES_REQUEST, request));
  }
}
