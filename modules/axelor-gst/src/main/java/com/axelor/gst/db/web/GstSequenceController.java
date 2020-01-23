package com.axelor.gst.db.web;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.db.service.GstSequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstSequenceController {

  @Inject GstSequenceService sequenceService;

  public void setPartySequence(ActionRequest request, ActionResponse response) {

    Party party = request.getContext().asType(Party.class);
    Sequence sequence = new Sequence();
    sequenceService = new GstSequenceService();
    if (party.getReference() == null) {
      MetaModel m = Beans.get(MetaModelRepository.class).findByName("Party");
 
      sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", m).fetchOne();
      int n = Integer.parseInt(sequence.getNextNumber());

      String nextnumber = "" + n;
      int size = nextnumber.length();

      for (int i = size; i <= sequence.getPadding(); i++) {
        nextnumber = "0" + nextnumber;
      }
      String s = sequence.getPrefix() + nextnumber + sequence.getSufix();
      n = n + 1;
      String num = "" + n;
      sequence.setNextNumber(num);
      party.setReference(s);
    }
    response.setValues(party);
    sequenceService.save(sequence);
  }
}
