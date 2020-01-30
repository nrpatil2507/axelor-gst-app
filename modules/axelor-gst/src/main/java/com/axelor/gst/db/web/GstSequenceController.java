package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class GstSequenceController {

  @Inject private SequenceRepository sequenceRepo;

  @Transactional
  public void setSequenceData(ActionRequest request, ActionResponse response) {
    Sequence sequence = request.getContext().asType(Sequence.class);

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

    response.setValue("nextNumber", newNextNumber);
  }

  @Transactional
  public void setPartySequence(ActionRequest request, ActionResponse response) {

    Party party = request.getContext().asType(Party.class);
    Sequence sequence = new Sequence();
    if (party.getReference() == null) {
      sequence =
          Beans.get(SequenceRepository.class)
              .all()
              .filter("self.model.name = ?", "Party")
              .fetchOne();
      if(sequence!=null)
      {
    	String suffix=sequence.getSufix();
    	String prefix=sequence.getPrefix();
    	String nextNumber;
    	int padding=sequence.getPadding();
    	int paddingvalue = Integer.parseInt((sequence.getNextNumber().substring(prefix.length(), prefix.length()+padding)))+1;
		String newpadding =Integer.toString(paddingvalue);
		while(newpadding.length()<padding){
			newpadding="0"+newpadding;
		}
		if(suffix==null){
			 nextNumber=prefix+newpadding;
		}
		else {
			nextNumber=prefix+newpadding+suffix;
		}
		sequence.setNextNumber(nextNumber);
		sequenceRepo.save(sequence);
		party.setReference(nextNumber);
		response.setValues(party);
      }
      else
      {
    	 response.setError("sequence for party was not generated"); 
      }
    }
  }

  @Transactional
  public void setInvoiceSequence(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Sequence sequence = new Sequence();
    if (invoice.getReference() == null) {
      sequence =
          Beans.get(SequenceRepository.class)
              .all()
              .filter("self.model.name = ?", "Invoice")
              .fetchOne();
      if(sequence!=null)
      {
    	String suffix=sequence.getSufix();
    	String prefix=sequence.getPrefix();
    	String nextNumber;
    	int padding=sequence.getPadding();
    	int paddingvalue = Integer.parseInt((sequence.getNextNumber().substring(prefix.length(), prefix.length()+padding)))+1;
		String newpadding =Integer.toString(paddingvalue);
		while(newpadding.length()<padding){
			newpadding="0"+newpadding;
		}
		if(suffix==null){
			 nextNumber=prefix+newpadding;
		}
		else {
			nextNumber=prefix+newpadding+suffix;
		}
		sequence.setNextNumber(nextNumber);
		sequenceRepo.save(sequence);
		invoice.setReference(nextNumber);
		response.setValues(invoice);
      }
      else
      {
    	 response.setError("sequence for invoice was not generated"); 
      }
    }
  }
}
