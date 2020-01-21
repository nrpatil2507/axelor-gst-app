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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "GST_PARTY", indexes = { @Index(columnList = "name") })
public class Party extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GST_PARTY_SEQ")
	@SequenceGenerator(name = "GST_PARTY_SEQ", sequenceName = "GST_PARTY_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Party Sequence", readonly = true)
	private String reference;

	@Widget(title = "Party Name")
	@NotNull
	private String name;

	@Widget(title = "Party Type", selection = "party.type.selection")
	@NotNull
	private String type = "company";

	@Widget(title = "Customer")
	private Boolean isCustomer = Boolean.FALSE;

	@Widget(title = "Supplier")
	private Boolean isSupplier = Boolean.FALSE;

	@Widget(title = "GSTIN NO")
	@Size(min = 15, max = 15)
	private String gstin;

	@Widget(title = "Contact List")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Contact> contactList;

	@Widget(title = "Address List")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Address> addressList;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Party() {
	}

	public Party(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsCustomer() {
		return isCustomer == null ? Boolean.FALSE : isCustomer;
	}

	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

	public Boolean getIsSupplier() {
		return isSupplier == null ? Boolean.FALSE : isSupplier;
	}

	public void setIsSupplier(Boolean isSupplier) {
		this.isSupplier = isSupplier;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

	/**
	 * Add the given {@link Contact} item to the {@code contactList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addContactListItem(Contact item) {
		if (getContactList() == null) {
			setContactList(new ArrayList<>());
		}
		getContactList().add(item);
	}

	/**
	 * Remove the given {@link Contact} item from the {@code contactList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeContactListItem(Contact item) {
		if (getContactList() == null) {
			return;
		}
		getContactList().remove(item);
	}

	/**
	 * Clear the {@code contactList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearContactList() {
		if (getContactList() != null) {
			getContactList().clear();
		}
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	/**
	 * Add the given {@link Address} item to the {@code addressList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addAddressListItem(Address item) {
		if (getAddressList() == null) {
			setAddressList(new ArrayList<>());
		}
		getAddressList().add(item);
	}

	/**
	 * Remove the given {@link Address} item from the {@code addressList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeAddressListItem(Address item) {
		if (getAddressList() == null) {
			return;
		}
		getAddressList().remove(item);
	}

	/**
	 * Clear the {@code addressList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearAddressList() {
		if (getAddressList() != null) {
			getAddressList().clear();
		}
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
		if (!(obj instanceof Party)) return false;

		final Party other = (Party) obj;
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
			.add("reference", getReference())
			.add("name", getName())
			.add("type", getType())
			.add("isCustomer", getIsCustomer())
			.add("isSupplier", getIsSupplier())
			.add("gstin", getGstin())
			.omitNullValues()
			.toString();
	}
}
