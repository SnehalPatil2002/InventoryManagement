package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Projects.
 */
@Entity
@Table(name = "projects")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Projects implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "order_quantity")
    private Long orderQuantity;

    @Column(name = "final_total")
    private Double finalTotal;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = { "productRawMaterials", "consumptionDetails", "productPrices", "project" }, allowSetters = true)
    private Set<Products> products = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = { "project", "product", "productionLine" }, allowSetters = true)
    private Set<ConsumptionDetails> consumptionDetails = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = { "project", "product" }, allowSetters = true)
    private Set<ProductPrice> productPrices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Projects id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Projects projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Projects startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Projects endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getOrderQuantity() {
        return this.orderQuantity;
    }

    public Projects orderQuantity(Long orderQuantity) {
        this.setOrderQuantity(orderQuantity);
        return this;
    }

    public void setOrderQuantity(Long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Double getFinalTotal() {
        return this.finalTotal;
    }

    public Projects finalTotal(Double finalTotal) {
        this.setFinalTotal(finalTotal);
        return this;
    }

    public void setFinalTotal(Double finalTotal) {
        this.finalTotal = finalTotal;
    }

    public Set<Products> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Products> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setProject(null));
        }
        if (products != null) {
            products.forEach(i -> i.setProject(this));
        }
        this.products = products;
    }

    public Projects products(Set<Products> products) {
        this.setProducts(products);
        return this;
    }

    public Projects addProducts(Products products) {
        this.products.add(products);
        products.setProject(this);
        return this;
    }

    public Projects removeProducts(Products products) {
        this.products.remove(products);
        products.setProject(null);
        return this;
    }

    public Set<ConsumptionDetails> getConsumptionDetails() {
        return this.consumptionDetails;
    }

    public void setConsumptionDetails(Set<ConsumptionDetails> consumptionDetails) {
        if (this.consumptionDetails != null) {
            this.consumptionDetails.forEach(i -> i.setProject(null));
        }
        if (consumptionDetails != null) {
            consumptionDetails.forEach(i -> i.setProject(this));
        }
        this.consumptionDetails = consumptionDetails;
    }

    public Projects consumptionDetails(Set<ConsumptionDetails> consumptionDetails) {
        this.setConsumptionDetails(consumptionDetails);
        return this;
    }

    public Projects addConsumptionDetails(ConsumptionDetails consumptionDetails) {
        this.consumptionDetails.add(consumptionDetails);
        consumptionDetails.setProject(this);
        return this;
    }

    public Projects removeConsumptionDetails(ConsumptionDetails consumptionDetails) {
        this.consumptionDetails.remove(consumptionDetails);
        consumptionDetails.setProject(null);
        return this;
    }

    public Set<ProductPrice> getProductPrices() {
        return this.productPrices;
    }

    public void setProductPrices(Set<ProductPrice> productPrices) {
        if (this.productPrices != null) {
            this.productPrices.forEach(i -> i.setProject(null));
        }
        if (productPrices != null) {
            productPrices.forEach(i -> i.setProject(this));
        }
        this.productPrices = productPrices;
    }

    public Projects productPrices(Set<ProductPrice> productPrices) {
        this.setProductPrices(productPrices);
        return this;
    }

    public Projects addProductPrice(ProductPrice productPrice) {
        this.productPrices.add(productPrice);
        productPrice.setProject(this);
        return this;
    }

    public Projects removeProductPrice(ProductPrice productPrice) {
        this.productPrices.remove(productPrice);
        productPrice.setProject(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projects)) {
            return false;
        }
        return id != null && id.equals(((Projects) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Projects{" +
            "id=" + getId() +
            ", projectName='" + getProjectName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", orderQuantity=" + getOrderQuantity() +
            ", finalTotal=" + getFinalTotal() +
            "}";
    }
}
