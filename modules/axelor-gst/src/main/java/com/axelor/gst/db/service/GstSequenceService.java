package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;

public interface GstSequenceService {

  public String setSequence(Sequence sequence);

  public void setReference(Sequence sequence);
}
