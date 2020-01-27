package com.axelor.gst.db.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface GstInvoiceService {

  public Invoice setGstAmount(Invoice invoice);

  public Invoice setPartyContact(Invoice invoice);

  public Invoice setInvoiceAdd(Invoice invoice);

  public Invoice setShiipingAdd(Invoice invoice);

  public InvoiceLine calculateInvocieLineGst(InvoiceLine invoiceLine, Invoice invoice);

  public InvoiceLine setSelectedProductInvoice(InvoiceLine invoiceLine);
}
