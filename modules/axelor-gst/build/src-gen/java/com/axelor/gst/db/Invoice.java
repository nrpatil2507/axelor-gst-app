/*
 * Axelor Business Solutions
 * 
 * Copyright (C) 2005-2020 Axelor (<http://axelor.com>).
 * 
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.gst.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "GST_INVOICE", indexes = { @Index(columnList = "company"), @Index(columnList = "party"), @Index(columnList = "party_contact"), @Index(columnList = "invoice_address"), @Index(columnList = "shipping_address") })
public class Invoice extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GST_INVOICE_SEQ")
	@SequenceGenerator(name = "GST_INVOICE_SEQ", sequenceName = "GST_INVOICE_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Invoice Status", readonly = true, selection = "invoice.status.selection")
	private String status = "draft";

	@Widget(title = "Company")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Invoice Sequence", readonly = true)
	private String reference;

	@Widget(title = "Invoice Date")
	private LocalDateTime invoiceDateT;

	@Widget(title = "Priority")
	private Integer priority = 0;

	@Widget(title = "Party")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Party party;

	@Widget(title = "Party Contact")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Contact partyContact;

	@Widget(title = "Invoice Address")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address invoiceAddress;

	@Widget(title = "Shipping Address")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address shippingAddress;

	@Widget(title = "Net Amount", readonly = true)
	private BigDecimal netAmount = BigDecimal.ZERO;

	@Widget(title = "Net IGST", readonly = true)
	private BigDecimal netIgst = BigDecimal.ZERO;

	@Widget(title = "Net CGST", readonly = true)
	private BigDecimal netCgst = BigDecimal.ZERO;

	@Widget(title = "Net SGST", readonly = true)
	private BigDecimal netSgst = BigDecimal.ZERO;

	@Widget(title = "Gross Amount", readonly = true)
	private BigDecimal grossAmount = BigDecimal.ZERO;

	@Widget(title = "Invoice Items List")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InvoiceLine> invoiceItemsList;

	@Widget(title = "Invoice Address As Shipping")
	private Boolean isInvoiceAddAsShipping = Boolean.TRUE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Invoice() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public LocalDateTime getInvoiceDateT() {
		return invoiceDateT;
	}

	public void setInvoiceDateT(LocalDateTime invoiceDateT) {
		this.invoiceDateT = invoiceDateT;
	}

	public Integer getPriority() {
		return priority == null ? 0 : priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public Contact getPartyContact() {
		return partyContact;
	}

	public void setPartyContact(Contact partyContact) {
		this.partyContact = partyContact;
	}

	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public BigDecimal getNetAmount() {
		return netAmount == null ? BigDecimal.ZERO : netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getNetIgst() {
		return netIgst == null ? BigDecimal.ZERO : netIgst;
	}

	public void setNetIgst(BigDecimal netIgst) {
		this.netIgst = netIgst;
	}

	public BigDecimal getNetCgst() {
		return netCgst == null ? BigDecimal.ZERO : netCgst;
	}

	public void setNetCgst(BigDecimal netCgst) {
		this.netCgst = netCgst;
	}

	public BigDecimal getNetSgst() {
		return netSgst == null ? BigDecimal.ZERO : netSgst;
	}

	public void setNetSgst(BigDecimal netSgst) {
		this.netSgst = netSgst;
	}

	public BigDecimal getGrossAmount() {
		return grossAmount == null ? BigDecimal.ZERO : grossAmount;
	}

	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}

	public List<InvoiceLine> getInvoiceItemsList() {
		return invoiceItemsList;
	}

	public void setInvoiceItemsList(List<InvoiceLine> invoiceItemsList) {
		this.invoiceItemsList = invoiceItemsList;
	}

	/**
	 * Add the given {@link InvoiceLine} item to the {@code invoiceItemsList}.
	 *
	 * <p>
	 * It sets {@code item.invoice = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addInvoiceItemsListItem(InvoiceLine item) {
		if (getInvoiceItemsList() == null) {
			setInvoiceItemsList(new ArrayList<>());
		}
		getInvoiceItemsList().add(item);
		item.setInvoice(this);
	}

	/**
	 * Remove the given {@link InvoiceLine} item from the {@code invoiceItemsList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeInvoiceItemsListItem(InvoiceLine item) {
		if (getInvoiceItemsList() == null) {
			return;
		}
		getInvoiceItemsList().remove(item);
	}

	/**
	 * Clear the {@code invoiceItemsList} collection.
	 *
	 * <p>
	 * If you have to query {@link InvoiceLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearInvoiceItemsList() {
		if (getInvoiceItemsList() != null) {
			getInvoiceItemsList().clear();
		}
	}

	public Boolean getIsInvoiceAddAsShipping() {
		return isInvoiceAddAsShipping == null ? Boolean.FALSE : isInvoiceAddAsShipping;
	}

	public void setIsInvoiceAddAsShipping(Boolean isInvoiceAddAsShipping) {
		this.isInvoiceAddAsShipping = isInvoiceAddAsShipping;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Invoice)) return false;

		final Invoice other = (Invoice) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("status", getStatus())
			.add("reference", getReference())
			.add("invoiceDateT", getInvoiceDateT())
			.add("priority", getPriority())
			.add("netAmount", getNetAmount())
			.add("netIgst", getNetIgst())
			.add("netCgst", getNetCgst())
			.add("netSgst", getNetSgst())
			.add("grossAmount", getGrossAmount())
			.add("isInvoiceAddAsShipping", getIsInvoiceAddAsShipping())
			.omitNullValues()
			.toString();
	}
}
