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
    if (party != null) {
      List<Contact> contactList = party.getContactList();
      if (contactList != null) {
        for (Contact contact : contactList) {
          if (contact.getType().equals("primary")) {
            response.setValue("partyContact", contact != null ? contact : null);
          }
        }
      }
    }
  }

  public void setInvoiceAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    if (party != null) {
      List<Address> addressList = party.getAddressList();
      if (addressList != null) {
        for (Address address : addressList) {
          if (address.getType().equals("invoice")) {
            response.setValue("invoiceAddress", address);
          } else if (address.getType().equals("default")) {
            response.setValue("invoiceAddress", address);
          }
        }
      }
    }
  }

  public void setShippingAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    if (party != null) {
      if (invoice.getIsInvoiceAddAsShipping() == true) {
        response.setValue("shippingAddress", invoice.getInvoiceAddress());
      } else {
        List<Address> addressList = party.getAddressList();
        if (addressList != null) {
          for (Address address : addressList) {
            if (address.getType().equals("shipping")) {
              response.setValue("shippingAddress", address);
            } else if (address.getType().equals("default")) {
              response.setValue("shippingAddress", address);
            }
          }
        }
      }
    }
  }

  public void setTotalAmount(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);

    List<InvoiceLine> invoiceLineList = invoice.getInvoiceItemsList();
    float igst = 0, sgst = 0, cgst = 0, grossAmount = 0, netAmount = 0;

    if (invoiceLineList != null) {
      for (InvoiceLine invoiceLine : invoiceLineList) {
        igst = igst + invoiceLine.getIgst().floatValue();
        sgst = sgst + invoiceLine.getSgst().floatValue();
        cgst = cgst + invoiceLine.getCgst().floatValue();
        netAmount = netAmount + invoiceLine.getNetAmount().floatValue();
        grossAmount = grossAmount + invoiceLine.getGrossAmount().floatValue();
      }
      response.setValue("netIgst", igst);
      response.setValue("netCgst", cgst);
      response.setValue("netSgst", sgst);
      response.setValue("netAmount", netAmount);
      response.setValue("grossAmount", grossAmount);
    }
  }
}
