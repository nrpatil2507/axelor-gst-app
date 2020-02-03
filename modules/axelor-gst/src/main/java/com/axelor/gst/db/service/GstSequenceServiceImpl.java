package com.axelor.gst.db.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class GstSequenceServiceImpl implements GstSequenceService {
  @Inject private SequenceRepository sequenceRepo;

  @Override
  @Transactional
  public String setSequence(Sequence sequence) {
    String suffix = sequence.getSufix();
    String prefix = sequence.getPrefix();
    String newNextNumber = null;
    int padding = (int) sequence.getPadding();
    String newPadding = "";

    for (int i = 0; i < padding; i++) {
      newPadding = newPadding + "0";
    }
    if (suffix == null) {
      newNextNumber = prefix + newPadding;
    } else {
      newNextNumber = prefix + newPadding + suffix;
    }
    sequence.setNextNumber(newNextNumber);
    return newNextNumber;
  }

  @Transactional
  public void setReference(Sequence sequence) {
    if (sequence != null) {
      String suffix = sequence.getSufix();
      String prefix = sequence.getPrefix();
      String nextNumber;
      int paddingvalue =
          Integer.parseInt(
                  (sequence
                      .getNextNumber()
                      .substring(prefix.length(), prefix.length() + sequence.getPadding())))
              + 1;
      String newpadding = Integer.toString(paddingvalue);
      while (newpadding.length() < sequence.getPadding()) {
        newpadding = "0" + newpadding;
      }
      if (suffix == null) {
        nextNumber = prefix + newpadding;
      } else {
        nextNumber = prefix + newpadding + suffix;
      }
      sequence.setNextNumber(nextNumber);
      sequenceRepo.save(sequence);
    }
  }
}
