package com.axelor.gst.db.service;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
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
  public InvoiceLine calculateInvoiceLineGst(InvoiceLine invoiceLine, Invoice invoice) {
    Address address = invoice.getInvoiceAddress();
    Company company = invoice.getCompany();
    Address address2 = company.getAddress();
    BigDecimal gstAmount;
    BigDecimal invoiceCgst;

    BigDecimal grossAmount;
    gstAmount =
        invoiceLine
            .getNetAmount()
            .multiply(invoiceLine.getGstRate())
            .divide(BigDecimal.valueOf(100));

    if (address.getState() == address2.getState()) {
      invoiceCgst = gstAmount.divide(BigDecimal.valueOf(2));
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
    if (product.getGstRate().compareTo(BigDecimal.ZERO) != 0) {
      invoiceLine.setGstRate(product.getGstRate());
    }
    if (product.getHsbn().equals(null)) {
      invoiceLine.setHsbn(product.getHsbn());
    }
    if (product.getSalePrice().compareTo(BigDecimal.ZERO) != 0) {
      invoiceLine.setPrice(product.getSalePrice());
    }
    if (invoiceLine.getQty() == 0) {
      invoiceLine.setQty(1);
    }
    invoiceLine.setItem("[" + product.getCode() + "]" + product.getName());
    invoiceLine.setNetAmount(
        invoiceLine.getPrice().multiply(BigDecimal.valueOf(invoiceLine.getQty())));
    return invoiceLine;
  }

  @Override
  public List<InvoiceLine> setInvoiceDetailOnChange(Invoice invoice) {
    List<InvoiceLine> invoiceLineList = invoice.getInvoiceItemsList();
    List<InvoiceLine> invoiceline = new ArrayList<InvoiceLine>();
    Party party = invoice.getParty();

    Company company = invoice.getCompany();
    Address address = company.getAddress();
    BigDecimal gstAmount = BigDecimal.ZERO;
    BigDecimal invoiceIgst = BigDecimal.ZERO;
    BigDecimal invoiceCgst = BigDecimal.ZERO;
    BigDecimal grossAmount = BigDecimal.ZERO;
    if (invoiceLineList != null) {
      if (party != null && company != null) {
        for (InvoiceLine invoiceLine : invoiceLineList) {
          gstAmount =
              invoiceLine
                  .getNetAmount()
                  .multiply(invoiceLine.getGstRate())
                  .divide(BigDecimal.valueOf(100));

          if (address.getState() == invoice.getInvoiceAddress().getState()) {
            invoiceCgst = gstAmount.divide(BigDecimal.valueOf(2));
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
      if (contactList != null) {
        for (Contact contact : contactList) {
          if (contact.getType().equals("primary")) {
            invoice.setPartyContact(contact);
          }
        }
      }
      List<Address> addressList = party.getAddressList();
      if (addressList != null) {
        for (Address address : addressList) {
          if (address.getType().equals("invoice")) {
            invoice.setInvoiceAddress(address);
          } else if (address.getType().equals("default")) {
            invoice.setInvoiceAddress(address);
          }
        }
      }
      if (invoice.getIsInvoiceAddAsShipping() == true) {
        invoice.setShippingAddress(invoice.getInvoiceAddress());
      } else {
        if (addressList != null) {
          for (Address address : addressList) {
            if (address.getType().equals("shipping")) {
              invoice.setShippingAddress(address);
            } else if (address.getType().equals("default")) {
              invoice.setShippingAddress(address);
            }
          }
        }
      }
    }
    return invoice;
  }
}
