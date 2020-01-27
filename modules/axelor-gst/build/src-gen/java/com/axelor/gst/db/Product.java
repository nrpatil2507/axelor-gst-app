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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "GST_PRODUCT", indexes = { @Index(columnList = "name"), @Index(columnList = "code"), @Index(columnList = "category"), @Index(columnList = "image") })
public class Product extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GST_PRODUCT_SEQ")
	@SequenceGenerator(name = "GST_PRODUCT_SEQ", sequenceName = "GST_PRODUCT_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Product Name")
	@NotNull
	private String name;

	@Widget(title = "product Code")
	@NotNull
	private String code;

	@Widget(title = "HSBN")
	private String hsbn;

	@Widget(title = "Product Category")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProductCategory category;

	@Widget(title = "Product Sale Price")
	private BigDecimal salePrice = BigDecimal.ZERO;

	@Widget(title = "Product Cost Price")
	private BigDecimal costPrice = BigDecimal.ZERO;

	@Widget(title = "Product Image")
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile image;

	@Widget(title = "Gst Rate")
	private BigDecimal gstRate = BigDecimal.ZERO;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Product() {
	}

	public Product(String name, String code) {
		this.name = name;
		this.code = code;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHsbn() {
		return hsbn;
	}

	public void setHsbn(String hsbn) {
		this.hsbn = hsbn;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public BigDecimal getSalePrice() {
		return salePrice == null ? BigDecimal.ZERO : salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getCostPrice() {
		return costPrice == null ? BigDecimal.ZERO : costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public MetaFile getImage() {
		return image;
	}

	public void setImage(MetaFile image) {
		this.image = image;
	}

	public BigDecimal getGstRate() {
		return gstRate == null ? BigDecimal.ZERO : gstRate;
	}

	public void setGstRate(BigDecimal gstRate) {
		this.gstRate = gstRate;
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
		if (!(obj instanceof Product)) return false;

		final Product other = (Product) obj;
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
			.add("name", getName())
			.add("code", getCode())
			.add("hsbn", getHsbn())
			.add("salePrice", getSalePrice())
			.add("costPrice", getCostPrice())
			.add("gstRate", getGstRate())
			.omitNullValues()
			.toString();
	}
}
