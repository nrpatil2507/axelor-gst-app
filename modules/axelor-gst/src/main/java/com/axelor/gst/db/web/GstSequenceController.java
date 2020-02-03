package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.db.service.GstSequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class GstSequenceController {
  @Inject private GstSequenceService sequenceService;

  @Transactional
  public void setSequenceData(ActionRequest request, ActionResponse response) {
    Sequence sequence = request.getContext().asType(Sequence.class);
    Sequence isSequence =
        Beans.get(SequenceRepository.class)
            .all()
            .filter("self.model.name=?", sequence.getModel().getName())
            .fetchOne();
    if (isSequence == null) {
      String nextNumber = sequenceService.setSequence(sequence);
      response.setValue("nextNumber", nextNumber);
    }
  }

  @Transactional
  public void setPartySequence(ActionRequest request, ActionResponse response) {
    Party party = request.getContext().asType(Party.class);
    if (party.getReference() == null) {
      Sequence sequence =
          Beans.get(SequenceRepository.class)
              .all()
              .filter("self.model.name = ?", "Party")
              .fetchOne();
      if (sequence != null) {
        party.setReference(sequence.getNextNumber());
        response.setValues(party);
        sequenceService.setReference(sequence);
      } else {
        response.setError("sequence for Party was not found");
      }
    }
  }

  @Transactional
  public void setInvoiceSequence(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    if (invoice.getReference() == null) {
      Sequence sequence =
          Beans.get(SequenceRepository.class)
              .all()
              .filter("self.model.name = ?", "Invoice")
              .fetchOne();
      if (sequence != null) {
        invoice.setReference(sequence.getNextNumber());
        response.setValues(invoice);
        sequenceService.setReference(sequence);
      } else {
        response.setError("sequence for Invoice was not found");
      }
    }
  }
}
