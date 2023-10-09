package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DeliveryStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A SalesOrderd.
 */
@Entity
@Table(name = "sales_orderd")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalesOrderd implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "quantity_sold")
    private Long quantitySold;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "gst_percentage")
    private Double gstPercentage;

    @Column(name = "total_revenue")
    private Double totalRevenue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeliveryStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "salesOrderds" }, allowSetters = true)
    private Clients client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SalesOrderd id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public SalesOrderd orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Long getQuantitySold() {
        return this.quantitySold;
    }

    public SalesOrderd quantitySold(Long quantitySold) {
        this.setQuantitySold(quantitySold);
        return this;
    }

    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public SalesOrderd unitPrice(Double unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getGstPercentage() {
        return this.gstPercentage;
    }

    public SalesOrderd gstPercentage(Double gstPercentage) {
        this.setGstPercentage(gstPercentage);
        return this;
    }

    public void setGstPercentage(Double gstPercentage) {
        this.gstPercentage = gstPercentage;
    }

    public Double getTotalRevenue() {
        return this.totalRevenue;
    }

    public SalesOrderd totalRevenue(Double totalRevenue) {
        this.setTotalRevenue(totalRevenue);
        return this;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public DeliveryStatus getStatus() {
        return this.status;
    }

    public SalesOrderd status(DeliveryStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Clients getClient() {
        return this.client;
    }

    public void setClient(Clients clients) {
        this.client = clients;
    }

    public SalesOrderd client(Clients clients) {
        this.setClient(clients);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalesOrderd)) {
            return false;
        }
        return id != null && id.equals(((SalesOrderd) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalesOrderd{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", quantitySold=" + getQuantitySold() +
            ", unitPrice=" + getUnitPrice() +
            ", gstPercentage=" + getGstPercentage() +
            ", totalRevenue=" + getTotalRevenue() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
