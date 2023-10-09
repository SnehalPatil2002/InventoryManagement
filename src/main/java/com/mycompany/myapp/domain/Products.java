package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "available_qty")
    private Double availableQty;

    @OneToMany(mappedBy = "products")
    @JsonIgnoreProperties(value = { "products", "rawMaterial" }, allowSetters = true)
    private Set<ProductRawMaterials> productRawMaterials = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "project", "product", "productionLine" }, allowSetters = true)
    private Set<ConsumptionDetails> consumptionDetails = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = { "project", "product" }, allowSetters = true)
    private Set<ProductPrice> productPrices = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "consumptionDetails", "productPrices" }, allowSetters = true)
    private Projects project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Products id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public Products productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public Products totalPrice(Double totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getAvailableQty() {
        return this.availableQty;
    }

    public Products availableQty(Double availableQty) {
        this.setAvailableQty(availableQty);
        return this;
    }

    public void setAvailableQty(Double availableQty) {
        this.availableQty = availableQty;
    }

    public Set<ProductRawMaterials> getProductRawMaterials() {
        return this.productRawMaterials;
    }

    public void setProductRawMaterials(Set<ProductRawMaterials> productRawMaterials) {
        if (this.productRawMaterials != null) {
            this.productRawMaterials.forEach(i -> i.setProducts(null));
        }
        if (productRawMaterials != null) {
            productRawMaterials.forEach(i -> i.setProducts(this));
        }
        this.productRawMaterials = productRawMaterials;
    }

    public Products productRawMaterials(Set<ProductRawMaterials> productRawMaterials) {
        this.setProductRawMaterials(productRawMaterials);
        return this;
    }

    public Products addProductRawMaterials(ProductRawMaterials productRawMaterials) {
        this.productRawMaterials.add(productRawMaterials);
        productRawMaterials.setProducts(this);
        return this;
    }

    public Products removeProductRawMaterials(ProductRawMaterials productRawMaterials) {
        this.productRawMaterials.remove(productRawMaterials);
        productRawMaterials.setProducts(null);
        return this;
    }

    public Set<ConsumptionDetails> getConsumptionDetails() {
        return this.consumptionDetails;
    }

    public void setConsumptionDetails(Set<ConsumptionDetails> consumptionDetails) {
        if (this.consumptionDetails != null) {
            this.consumptionDetails.forEach(i -> i.setProduct(null));
        }
        if (consumptionDetails != null) {
            consumptionDetails.forEach(i -> i.setProduct(this));
        }
        this.consumptionDetails = consumptionDetails;
    }

    public Products consumptionDetails(Set<ConsumptionDetails> consumptionDetails) {
        this.setConsumptionDetails(consumptionDetails);
        return this;
    }

    public Products addConsumptionDetails(ConsumptionDetails consumptionDetails) {
        this.consumptionDetails.add(consumptionDetails);
        consumptionDetails.setProduct(this);
        return this;
    }

    public Products removeConsumptionDetails(ConsumptionDetails consumptionDetails) {
        this.consumptionDetails.remove(consumptionDetails);
        consumptionDetails.setProduct(null);
        return this;
    }

    public Set<ProductPrice> getProductPrices() {
        return this.productPrices;
    }

    public void setProductPrices(Set<ProductPrice> productPrices) {
        if (this.productPrices != null) {
            this.productPrices.forEach(i -> i.setProduct(null));
        }
        if (productPrices != null) {
            productPrices.forEach(i -> i.setProduct(this));
        }
        this.productPrices = productPrices;
    }

    public Products productPrices(Set<ProductPrice> productPrices) {
        this.setProductPrices(productPrices);
        return this;
    }

    public Products addProductPrice(ProductPrice productPrice) {
        this.productPrices.add(productPrice);
        productPrice.setProduct(this);
        return this;
    }

    public Products removeProductPrice(ProductPrice productPrice) {
        this.productPrices.remove(productPrice);
        productPrice.setProduct(null);
        return this;
    }

    public Projects getProject() {
        return this.project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public Products project(Projects projects) {
        this.setProject(projects);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Products)) {
            return false;
        }
        return id != null && id.equals(((Products) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", availableQty=" + getAvailableQty() +
            "}";
    }
}
