package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class GstSequenceController {

	@Inject
	private SequenceRepository sequenceRepo;

	@Transactional
	public void setPartySequence(ActionRequest request, ActionResponse response) {

		Party party = request.getContext().asType(Party.class);
		Sequence sequence = new Sequence();

		if (party.getReference() == null) {

			MetaModel m = Beans.get(MetaModelRepository.class).findByName("Party");

			sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", m).fetchOne();
			
			if (sequence != null) {
				int n = Integer.parseInt(sequence.getNextNumber());
				String nextnumber = "" + n;
				for (int i = nextnumber.length(); i <= sequence.getPadding(); i++) {
					nextnumber = "0" + nextnumber;
				}
				n = n + 1;
				sequence.setNextNumber(String.valueOf(n));
				String partySequence = sequence.getPrefix() + nextnumber + sequence.getSufix();
				party.setReference(partySequence);
				sequenceRepo.persist(sequence);
			}
			response.setValues(party);
		} else {
			response.setFlash("sequence for invoice model was not generated");
		}
	}

	@Transactional
	public void setInvoiceSequence(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		Sequence sequence = new Sequence();
		String partySequence;
		if (invoice.getReference() == null) {
			MetaModel m = Beans.get(MetaModelRepository.class).findByName("Invoice");

			sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", m).fetchOne();
			if (sequence != null) {
				int n = Integer.parseInt(sequence.getNextNumber());

				String nextnumber = "" + n;
				for (int i = nextnumber.length(); i <= sequence.getPadding(); i++) {
					nextnumber = "0" + nextnumber;
				}
				n = n + 1;
				sequence.setNextNumber(String.valueOf(n));

				if (sequence.getSufix() != null && nextnumber != null) {
					partySequence = sequence.getPrefix() + nextnumber + sequence.getSufix();
				} else
					partySequence = sequence.getPrefix() + nextnumber;
				invoice.setReference(partySequence);
				sequenceRepo.persist(sequence);
				response.setValues(invoice);
			} else {
				response.setFlash("sequence for invoice model was not generated");
			}
		}
	}
}
