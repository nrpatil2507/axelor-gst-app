package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.service.GstInvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstInvoiceController {

  @Inject private GstInvoiceService gstInvoiceService;

  public void setPrimaryContact(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = gstInvoiceService.setPartyContact(invoice);
    response.setValue("partyContact", invoice.getPartyContact());
  }

  public void setInvoiceAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = gstInvoiceService.setInvoiceAdd(invoice);
    response.setValue("invoiceAddress", invoice.getInvoiceAddress());
  }

  public void setShippingAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = gstInvoiceService.setShiipingAdd(invoice);
    response.setValue("shippingAddress", invoice.getShippingAddress());
  }

  public void setTotalAmount(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = gstInvoiceService.setGstAmount(invoice);
    response.setValue("netIgst", invoice.getNetIgst());
    response.setValue("netCgst", invoice.getNetCgst());
    response.setValue("netSgst", invoice.getNetSgst());
    response.setValue("netAmount", invoice.getNetAmount());
    response.setValue("grossAmount", invoice.getGrossAmount());
  }

  @SuppressWarnings("deprecation")
  public void calculateGst(ActionRequest request, ActionResponse response) {

    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParentContext().asType(Invoice.class);
    invoiceLine = gstInvoiceService.calculateInvocieLineGst(invoiceLine, invoice);
    response.setValue("igst", invoiceLine.getIgst());
    response.setValue("cgst", invoiceLine.getCgst());
    response.setValue("sgst", invoiceLine.getSgst());
    response.setValue("grossAmount", invoiceLine.getGrossAmount());
  }
}
