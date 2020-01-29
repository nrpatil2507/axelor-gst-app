package com.axelor.gst.db.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import java.util.List;

public interface GstInvoiceService {

  public Invoice setGstAmount(Invoice invoice);

  public Invoice setPartyDetail(Invoice invoice);

  public InvoiceLine calculateInvoiceLineGst(InvoiceLine invoiceLine, Invoice invoice);

  public InvoiceLine setSelectedProductInvoice(InvoiceLine invoiceLine);

  public List<InvoiceLine> setInvoiceDetailOnChange(Invoice invoice);
}
