package com.axelor.gst.db.service;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import java.math.BigDecimal;
import java.util.List;

public class GstInvoiceServiceImpl implements GstInvoiceService {

  @Override
  public Invoice setGstAmount(Invoice invoice) {
    float igst = 0, sgst = 0, cgst = 0, grossAmount = 0, netAmount = 0;
    List<InvoiceLine> invoiceLineList = invoice.getInvoiceItemsList();

    if (invoiceLineList != null) {
      for (InvoiceLine invoiceLine : invoiceLineList) {
        igst = igst + invoiceLine.getIgst().floatValue();
        sgst = sgst + invoiceLine.getSgst().floatValue();
        cgst = cgst + invoiceLine.getCgst().floatValue();
        netAmount = netAmount + invoiceLine.getNetAmount().floatValue();
        grossAmount = grossAmount + invoiceLine.getGrossAmount().floatValue();
      }
    }
    invoice.setNetIgst(BigDecimal.valueOf(igst));
    invoice.setNetAmount(BigDecimal.valueOf(netAmount));
    invoice.setNetSgst(BigDecimal.valueOf(sgst));
    invoice.setNetCgst(BigDecimal.valueOf(cgst));
    invoice.setGrossAmount(BigDecimal.valueOf(grossAmount));
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
}
