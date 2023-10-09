package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A ProductionLine.
 */
@Entity
@Table(name = "production_line")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @JsonIgnoreProperties(value = { "productRawMaterials", "consumptionDetails", "productPrices", "project" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Products product;

    @OneToMany(mappedBy = "productionLine")
    @JsonIgnoreProperties(value = { "rawMaterial", "product", "productionLine" }, allowSetters = true)
    private Set<StockRequest> stockRequests = new HashSet<>();

    @OneToMany(mappedBy = "productionLine")
    @JsonIgnoreProperties(value = { "project", "product", "productionLine" }, allowSetters = true)
    private Set<ConsumptionDetails> consumptionDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductionLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductionLine description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ProductionLine isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Products getProduct() {
        return this.product;
    }

    public void setProduct(Products products) {
        this.product = products;
    }

    public ProductionLine product(Products products) {
        this.setProduct(products);
        return this;
    }

    public Set<StockRequest> getStockRequests() {
        return this.stockRequests;
    }

    public void setStockRequests(Set<StockRequest> stockRequests) {
        if (this.stockRequests != null) {
            this.stockRequests.forEach(i -> i.setProductionLine(null));
        }
        if (stockRequests != null) {
            stockRequests.forEach(i -> i.setProductionLine(this));
        }
        this.stockRequests = stockRequests;
    }

    public ProductionLine stockRequests(Set<StockRequest> stockRequests) {
        this.setStockRequests(stockRequests);
        return this;
    }

    public ProductionLine addStockRequest(StockRequest stockRequest) {
        this.stockRequests.add(stockRequest);
        stockRequest.setProductionLine(this);
        return this;
    }

    public ProductionLine removeStockRequest(StockRequest stockRequest) {
        this.stockRequests.remove(stockRequest);
        stockRequest.setProductionLine(null);
        return this;
    }

    public Set<ConsumptionDetails> getConsumptionDetails() {
        return this.consumptionDetails;
    }

    public void setConsumptionDetails(Set<ConsumptionDetails> consumptionDetails) {
        if (this.consumptionDetails != null) {
            this.consumptionDetails.forEach(i -> i.setProductionLine(null));
        }
        if (consumptionDetails != null) {
            consumptionDetails.forEach(i -> i.setProductionLine(this));
        }
        this.consumptionDetails = consumptionDetails;
    }

    public ProductionLine consumptionDetails(Set<ConsumptionDetails> consumptionDetails) {
        this.setConsumptionDetails(consumptionDetails);
        return this;
    }

    public ProductionLine addConsumptionDetails(ConsumptionDetails consumptionDetails) {
        this.consumptionDetails.add(consumptionDetails);
        consumptionDetails.setProductionLine(this);
        return this;
    }

    public ProductionLine removeConsumptionDetails(ConsumptionDetails consumptionDetails) {
        this.consumptionDetails.remove(consumptionDetails);
        consumptionDetails.setProductionLine(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductionLine)) {
            return false;
        }
        return id != null && id.equals(((ProductionLine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductionLine{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
