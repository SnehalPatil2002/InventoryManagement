package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A ProductRawMaterials. This is domian file
 */
@Entity
@Table(name = "product_raw_materials")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductRawMaterials implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity_required")
    private Long quantityRequired;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productRawMaterials", "consumptionDetails", "productPrices", "project" }, allowSetters = true)
    private Products products;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productRawMaterials", "warehouse" }, allowSetters = true)
    private RawMaterial rawMaterial;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductRawMaterials id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantityRequired() {
        return this.quantityRequired;
    }

    public ProductRawMaterials quantityRequired(Long quantityRequired) {
        this.setQuantityRequired(quantityRequired);
        return this;
    }

    public void setQuantityRequired(Long quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public Products getProducts() {
        return this.products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public ProductRawMaterials products(Products products) {
        this.setProducts(products);
        return this;
    }

    public RawMaterial getRawMaterial() {
        return this.rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public ProductRawMaterials rawMaterial(RawMaterial rawMaterial) {
        this.setRawMaterial(rawMaterial);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductRawMaterials)) {
            return false;
        }
        return id != null && id.equals(((ProductRawMaterials) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductRawMaterials{" +
            "id=" + getId() +
            ", quantityRequired=" + getQuantityRequired() +
            "}";
    }
}
