package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;

public class GstSequenceService extends SequenceRepository {

  @Override
  public Sequence save(Sequence sequence) {
    return super.save(sequence);
  }
}
