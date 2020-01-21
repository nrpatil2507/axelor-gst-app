package com.axelor.gst.db.web;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.util.List;

public class GstInvoiceController {

  public void setPrimaryContact(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    List<Contact> contact = party.getContactList();

    for (Contact contact2 : contact) {
      if (contact2.getType().equals("primary")) {
        response.setValue("partyContact", contact2 != null ? contact2 : null);
      }
    }
  }

  public void setInvoiceAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    List<Address> address = party.getAddressList();

    for (Address address2 : address) {
      if (address2.getType().equals("invoice")) {
        response.setValue("invoiceAddress", address2);
      } else if (address2.getType().equals("default")) {
        response.setValue("invoiceAddress", address2);
      }
    }
  }

  public void setShippingAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    List<Address> address = party.getAddressList();

    if (invoice.getIsInvoiceAddAsShipping() == true) {
      response.setValue("shippingAddress", invoice.getInvoiceAddress());
    } else {
      for (Address address2 : address) {
        if (address2.getType().equals("shipping")) {
          response.setValue("invoiceAddress", address2);
        } else if (address2.getType().equals("default")) {
          response.setValue("invoiceAddress", address2);
        }
      }
    }
  }

  public void calculateGst(ActionRequest request, ActionResponse response) {

    // Invoice invoice=request.getContext().asType(Invoice.class);
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    response.setFlash(invoiceLine.getItem());
  }
}