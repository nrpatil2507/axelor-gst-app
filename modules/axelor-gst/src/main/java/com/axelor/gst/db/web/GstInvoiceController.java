package com.axelor.gst.db.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.service.GstInvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GstInvoiceController {

  @Inject private GstInvoiceService gstInvoiceService;
  @Inject ProductRepository productRepo;

  public void setPartyDetail(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = gstInvoiceService.setPartyDetail(invoice);
    response.setValue("shippingAddress", invoice.getShippingAddress());
    response.setValue("invoiceAddress", invoice.getInvoiceAddress());
    response.setValue("partyContact", invoice.getPartyContact());
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

  public void calculateGst(ActionRequest request, ActionResponse response) {

    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    invoiceLine = gstInvoiceService.calculateInvoiceLineGst(invoiceLine, invoice);
    response.setValue("igst", invoiceLine.getIgst());
    response.setValue("cgst", invoiceLine.getCgst());
    response.setValue("sgst", invoiceLine.getSgst());
    response.setValue("grossAmount", invoiceLine.getGrossAmount());
  }

  public void setInvoiceLineOnChangeParty(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    List<InvoiceLine> invoiceLineList = new ArrayList<>();
    invoiceLineList = gstInvoiceService.setInvoiceDetailOnChange(invoice);
    invoice = gstInvoiceService.setGstAmount(invoice);
    response.setValue("netIgst", invoice.getNetIgst());
    response.setValue("netCgst", invoice.getNetCgst());
    response.setValue("netSgst", invoice.getNetSgst());
    response.setValue("netAmount", invoice.getNetAmount());
    response.setValue("grossAmount", invoice.getGrossAmount());
    response.setValue("invoiceItemsList", invoiceLineList);
  }

  @SuppressWarnings("unchecked")
  public void getSeletedProducts(ActionRequest request, ActionResponse response) {
    List<Integer> productIdList = new ArrayList<>();
    productIdList = (List<Integer>) request.getContext().get("productIds");
    if (productIdList != null) {
      try {
        List<InvoiceLine> invoiceLineList = new ArrayList<>();
        for (Integer ProductId : productIdList) {
          Product product = productRepo.find(ProductId.longValue());
          System.out.println(product);
          InvoiceLine invoiceLine = new InvoiceLine();
          invoiceLine.setProduct(product);
          invoiceLine = gstInvoiceService.setSelectedProductInvoice(invoiceLine);
          invoiceLineList.add(invoiceLine);
        }
        response.setValue("invoiceItemsList", invoiceLineList);
      } catch (Exception e) {
      }
    }
  }
}
