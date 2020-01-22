package com.axelor.gst.db.web;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class GstInvoiceLineController {

  @SuppressWarnings("deprecation")
  public void calculateGst(ActionRequest request, ActionResponse response) {
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParentContext().asType(Invoice.class);

    Address address = invoice.getInvoiceAddress();
    Company company = invoice.getCompany();
    Address address2 = company.getAddress();

    float gstAmount;
    float invoiceCgst = 0;
    float invoiceSgst = 0;
    float grossAmount = 0;

    gstAmount =
        (invoiceLine.getNetAmount().floatValue() * invoiceLine.getGstRate().floatValue()) / 100;

    if (address.getState() == address2.getState()) {
      invoiceCgst = gstAmount / 2;
      invoiceSgst = gstAmount / 2;
      response.setValue("cgst", invoiceCgst);
      response.setValue("sgst", invoiceSgst);
      grossAmount = invoiceLine.getNetAmount().floatValue() + invoiceCgst + invoiceSgst;
    } else {
      response.setValue("igst", gstAmount);
      grossAmount = invoiceLine.getNetAmount().floatValue() + gstAmount;
    }
    response.setValue("grossAmount", grossAmount);
  }
}
