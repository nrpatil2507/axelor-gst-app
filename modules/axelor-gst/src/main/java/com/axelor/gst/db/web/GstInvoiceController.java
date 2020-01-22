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
    if (party != null) {
      List<Address> address = party.getAddressList();
      for (Address address2 : address) {
        if (address2.getType().equals("invoice")) {
          response.setValue("invoiceAddress", address2);
        } else if (address2.getType().equals("default")) {
          response.setValue("invoiceAddress", address2);
        }
      }
    }
  }

  public void setShippingAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    if (party != null) {
      List<Address> address = party.getAddressList();
      for (Address address2 : address) {
        if (address2.getType().equals("shipping")) {
          response.setValue("invoiceAddress", address2);
        } else if (address2.getType().equals("default")) {
        }
      }
    }
  }

  public void setTotalAmount(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);

    List<InvoiceLine> invoiceLine = invoice.getInvoiceItemsList();
    float igst = 0, sgst = 0, cgst = 0, grossAmount = 0, netAmount = 0;

    if (invoiceLine != null) {
      for (InvoiceLine invoiceLine2 : invoiceLine) {
        igst = igst + invoiceLine2.getIgst().floatValue();
        sgst = sgst + invoiceLine2.getSgst().floatValue();
        cgst = cgst + invoiceLine2.getCgst().floatValue();
        netAmount = netAmount + invoiceLine2.getNetAmount().floatValue();
        grossAmount = grossAmount + invoiceLine2.getGrossAmount().floatValue();
      }
      response.setValue("netIgst", igst);
      response.setValue("netCgst", cgst);
      response.setValue("netSgst", sgst);
      response.setValue("netAmount", netAmount);
      response.setValue("grossAmount", grossAmount);
    }
  }
}
