package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A ConsumptionDetails.
 */
@Entity
@Table(name = "consumption_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity_consumed")
    private Double quantityConsumed;

    @Column(name = "scrap_generated")
    private Double scrapGenerated;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products", "consumptionDetails", "productPrices" }, allowSetters = true)
    private Projects project;

    @ManyToOne
    @JsonIgnoreProperties(value = { "productRawMaterials", "consumptionDetails", "productPrices", "project" }, allowSetters = true)
    private Products product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "product", "stockRequests", "consumptionDetails" }, allowSetters = true)
    private ProductionLine productionLine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConsumptionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantityConsumed() {
        return this.quantityConsumed;
    }

    public ConsumptionDetails quantityConsumed(Double quantityConsumed) {
        this.setQuantityConsumed(quantityConsumed);
        return this;
    }

    public void setQuantityConsumed(Double quantityConsumed) {
        this.quantityConsumed = quantityConsumed;
    }

    public Double getScrapGenerated() {
        return this.scrapGenerated;
    }

    public ConsumptionDetails scrapGenerated(Double scrapGenerated) {
        this.setScrapGenerated(scrapGenerated);
        return this;
    }

    public void setScrapGenerated(Double scrapGenerated) {
        this.scrapGenerated = scrapGenerated;
    }

    public Projects getProject() {
        return this.project;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }

    public ConsumptionDetails project(Projects projects) {
        this.setProject(projects);
        return this;
    }

    public Products getProduct() {
        return this.product;
    }

    public void setProduct(Products products) {
        this.product = products;
    }

    public ConsumptionDetails product(Products products) {
        this.setProduct(products);
        return this;
    }

    public ProductionLine getProductionLine() {
        return this.productionLine;
    }

    public void setProductionLine(ProductionLine productionLine) {
        this.productionLine = productionLine;
    }

    public ConsumptionDetails productionLine(ProductionLine productionLine) {
        this.setProductionLine(productionLine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumptionDetails)) {
            return false;
        }
        return id != null && id.equals(((ConsumptionDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumptionDetails{" +
            "id=" + getId() +
            ", quantityConsumed=" + getQuantityConsumed() +
            ", scrapGenerated=" + getScrapGenerated() +
            "}";
    }
}
