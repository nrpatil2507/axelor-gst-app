package com.axelor.gst.db.service;

import com.axelor.common.ObjectUtils;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GstInvoiceServiceImpl implements GstInvoiceService {

  @Override
  public Invoice setGstAmount(Invoice invoice) {
    BigDecimal igst = BigDecimal.ZERO,
        sgst = BigDecimal.ZERO,
        cgst = BigDecimal.ZERO,
        grossAmount = BigDecimal.ZERO,
        netAmount = BigDecimal.ZERO;

    List<InvoiceLine> invoiceLineList = invoice.getInvoiceItemsList();
    if (invoiceLineList != null) {
      for (InvoiceLine invoiceLine : invoiceLineList) {
        igst = igst.add(invoiceLine.getIgst());
        sgst = sgst.add(invoiceLine.getSgst());
        cgst = cgst.add(invoiceLine.getCgst());
        netAmount = netAmount.add(invoiceLine.getNetAmount());
        grossAmount = grossAmount.add(invoiceLine.getGrossAmount());
      }
    }
    invoice.setNetIgst(igst);
    invoice.setNetAmount(netAmount);
    invoice.setNetSgst(sgst);
    invoice.setNetCgst(cgst);
    invoice.setGrossAmount(grossAmount);
    return invoice;
  }

  @Override
  public InvoiceLine calculateInvoiceLineGst(InvoiceLine invoiceLine, Boolean isIgst) {
    BigDecimal gstAmount;
    BigDecimal invoiceCgst;

    BigDecimal grossAmount;
    gstAmount =
        invoiceLine
            .getNetAmount()
            .multiply(invoiceLine.getGstRate())
            .divide(new BigDecimal(100));

    if (isIgst) {
      invoiceCgst = gstAmount.divide(new BigDecimal(2));
      invoiceLine.setCgst(invoiceCgst);
      invoiceLine.setSgst(invoiceCgst);
      grossAmount = invoiceLine.getNetAmount().add(invoiceCgst).add(invoiceCgst);
    } else {
      invoiceLine.setIgst(gstAmount);
      grossAmount = invoiceLine.getNetAmount().add(gstAmount);
    }
    invoiceLine.setGrossAmount(grossAmount);
    return invoiceLine;
  }

  @Override
  public InvoiceLine setSelectedProductInvoice(InvoiceLine invoiceLine) {

    Product product = invoiceLine.getProduct();
    if (product != null) {
      invoiceLine.setGstRate(product.getGstRate());
      invoiceLine.setHsbn(product.getHsbn());
      invoiceLine.setPrice(product.getSalePrice());
      invoiceLine.setQty(1);
      invoiceLine.setItem("[" + product.getCode() + "]" + product.getName());
      invoiceLine.setNetAmount(
          invoiceLine.getPrice().multiply(new BigDecimal(invoiceLine.getQty())));
    }
    return invoiceLine;
  }

  @Override
  public List<InvoiceLine> setInvoiceDetailOnChange(Invoice invoice) {
    List<InvoiceLine> invoiceLineList = invoice.getInvoiceItemsList();
    List<InvoiceLine> invoiceline = new ArrayList<InvoiceLine>();

    Party party = invoice.getParty();
    BigDecimal gstAmount = BigDecimal.ZERO;
    BigDecimal invoiceIgst = BigDecimal.ZERO;
    BigDecimal invoiceCgst = BigDecimal.ZERO;
    BigDecimal grossAmount = BigDecimal.ZERO;

    if (invoiceLineList != null) {
      if (party != null) {
        for (InvoiceLine invoiceLine : invoiceLineList) {
          gstAmount =
              invoiceLine
                  .getNetAmount()
                  .multiply(invoiceLine.getGstRate())
                  .divide(new BigDecimal(100));

          if (invoice.getCompany().getAddress().getState()
              == invoice.getInvoiceAddress().getState()) {
            invoiceCgst = gstAmount.divide(new BigDecimal(2));
            grossAmount = invoiceLine.getNetAmount().add(invoiceCgst).add(invoiceCgst);
          } else {
            invoiceIgst = gstAmount;
            grossAmount = invoiceLine.getNetAmount().add(gstAmount);
          }
          invoiceLine.setCgst(invoiceCgst);
          invoiceLine.setSgst(invoiceCgst);
          invoiceLine.setIgst(invoiceIgst);
          invoiceLine.setGrossAmount(grossAmount);
          invoiceline.add(invoiceLine);
        }
      }
    }
    return invoiceline;
  }

  @Override
  public Invoice setPartyDetail(Invoice invoice) {
    Party party = invoice.getParty();
    if (party != null) {
      List<Contact> contactList = party.getContactList();
      if (!ObjectUtils.isEmpty(contactList)) {
        for (Contact contact : contactList) {
          if (contact.getType().equals("primary")) {
            invoice.setPartyContact(contact);
          }
        }
      } else {
        invoice.setPartyContact(null);
      }
      List<Address> addressList = party.getAddressList();
      if (!ObjectUtils.isEmpty(addressList)) {
        for (Address address : addressList) {
          if (address.getType().equals("invoice") || address.getType().equals("default")) {
            invoice.setInvoiceAddress(address);
          }
        }
      } else {
        invoice.setInvoiceAddress(null);
      }
      if (!ObjectUtils.isEmpty(addressList)) {
        if (invoice.getIsInvoiceAddAsShipping() == true) {
          invoice.setShippingAddress(invoice.getInvoiceAddress());
        } else {
          for (Address address : addressList) {
            if (address.getType().equals("shipping") || address.getType().equals("default")) {
              invoice.setShippingAddress(address);
            }
          }
        }
      } else {
        invoice.setShippingAddress(null);
      }
    }
    return invoice;
  }
}
