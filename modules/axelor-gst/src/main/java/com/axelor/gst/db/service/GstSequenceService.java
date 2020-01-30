package com.axelor.gst.db.service;

import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class GstSequenceService {

  @Inject SequenceRepository sequenceRepo;

  @Transactional
  public void setPartySequence() {}
}
