package com.axelor.gst.db.service;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import java.math.BigDecimal;
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
  public Invoice setPartyContact(Invoice invoice) {
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
    }
    return invoice;
  }

  @Override
  public Invoice setInvoiceAdd(Invoice invoice) {
    Party party = invoice.getParty();
    if (party != null) {
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
    }
    return invoice;
  }

  @Override
  public Invoice setShiipingAdd(Invoice invoice) {
    Party party = invoice.getParty();
    if (party != null) {
      if (invoice.getIsInvoiceAddAsShipping() == true) {
        invoice.setShippingAddress(invoice.getInvoiceAddress());
      } else {
        List<Address> addressList = party.getAddressList();
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

  @Override
  public InvoiceLine calculateInvocieLineGst(InvoiceLine invoiceLine, Invoice invoice) {

    Address address = invoice.getInvoiceAddress();
    Company company = invoice.getCompany();
    Address address2 = company.getAddress();
    BigDecimal gstAmount;
    BigDecimal invoiceCgst;
    BigDecimal invoiceSgst;
    BigDecimal grossAmount;

    gstAmount =
        invoiceLine
            .getNetAmount()
            .multiply(invoiceLine.getGstRate())
            .divide(BigDecimal.valueOf(100));

    if (address.getState() == address2.getState()) {
      invoiceCgst = gstAmount.divide(gstAmount, 2);
      invoiceSgst = gstAmount.divide(gstAmount, 2);

      invoiceLine.setCgst(invoiceCgst);
      invoiceLine.setSgst(invoiceSgst);
      grossAmount = invoiceLine.getNetAmount().add(invoiceCgst).add(invoiceSgst);
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

    if (product.getGstRate() != new BigDecimal("0")) {
      invoiceLine.setGstRate(product.getGstRate());
    }
    if (product.getHsbn() != null) {
      invoiceLine.setHsbn(product.getHsbn());
    }
    if (product.getSalePrice() != null) {
      invoiceLine.setPrice(product.getSalePrice());
    }
    if (invoiceLine.getQty() == 0) {
      invoiceLine.setQty(1);
    }
    invoiceLine.setItem("[" + product.getCode() + "]" + product.getName());
    return invoiceLine;
  }
}
