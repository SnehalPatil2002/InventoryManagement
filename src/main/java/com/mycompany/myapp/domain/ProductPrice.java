package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A ProductPrice.
 */
@Entity
@Table(name = "product_price")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "raw_material_cost")
    private Double rawMaterialCost;

    @Column(name = "manufacturing_cost")
    private Double manufacturingCost;

    @Column(name = "labour_cost")
    private Double labourCost;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "consumptionDetails", "productPrices" }, allowSetters = true)
    private Projects project;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productRawMaterials", "consumptionDetails", "productPrices", "project" }, allowSetters = true)
    private Products product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductPrice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRawMaterialCost() {
        return this.rawMaterialCost;
    }

    public ProductPrice rawMaterialCost(Double rawMaterialCost) {
        this.setRawMaterialCost(rawMaterialCost);
        return this;
    }

    public void setRawMaterialCost(Double rawMaterialCost) {
        this.rawMaterialCost = rawMaterialCost;
    }

    public Double getManufacturingCost() {
        return this.manufacturingCost;
    }

    public ProductPrice manufacturingCost(Double manufacturingCost) {
        this.setManufacturingCost(manufacturingCost);
        return this;
    }

    public void setManufacturingCost(Double manufacturingCost) {
        this.manufacturingCost = manufacturingCost;
    }

    public Double getLabourCost() {
        return this.labourCost;
    }

    public ProductPrice labourCost(Double labourCost) {
        this.setLabourCost(labourCost);
        return this;
    }

    public void setLabourCost(Double labourCost) {
        this.labourCost = labourCost;
    }

    public Projects getProject() {
        return this.project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public ProductPrice project(Projects projects) {
        this.setProject(projects);
        return this;
    }

    public Products getProduct() {
        return this.product;
    }

    public void setProduct(Products products) {
        this.product = products;
    }

    public ProductPrice product(Products products) {
        this.setProduct(products);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductPrice)) {
            return false;
        }
        return id != null && id.equals(((ProductPrice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductPrice{" +
            "id=" + getId() +
            ", rawMaterialCost=" + getRawMaterialCost() +
            ", manufacturingCost=" + getManufacturingCost() +
            ", labourCost=" + getLabourCost() +
            "}";
    }
}
