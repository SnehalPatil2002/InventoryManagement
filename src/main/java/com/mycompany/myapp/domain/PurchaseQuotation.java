package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PurchaseQuotation.
 */
@Entity
@Table(name = "purchase_quotation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseQuotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "total_po_amount")
    private Double totalPOAmount;

    @Column(name = "total_gst_amount")
    private Double totalGSTAmount;

    @Column(name = "po_date")
    private Instant poDate;

    @Column(name = "expected_delivery_date")
    private Instant expectedDeliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private Status orderStatus;

    @OneToMany(mappedBy = "purchaseQuotation")
    @JsonIgnoreProperties(value = { "purchaseQuotation" }, allowSetters = true)
    private Set<PurchaseQuotationDetails> purchaseQuotationDetails = new HashSet<>();

    @OneToMany(mappedBy = "purchaseQuotation")
    @JsonIgnoreProperties(value = { "purchaseQuotation" }, allowSetters = true)
    private Set<OrderRecieved> orderRecieveds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "salesOrderds" }, allowSetters = true)
    private Clients clients;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseQuotation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public PurchaseQuotation referenceNumber(String referenceNumber) {
        this.setReferenceNumber(referenceNumber);
        return this;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Double getTotalPOAmount() {
        return this.totalPOAmount;
    }

    public PurchaseQuotation totalPOAmount(Double totalPOAmount) {
        this.setTotalPOAmount(totalPOAmount);
        return this;
    }

    public void setTotalPOAmount(Double totalPOAmount) {
        this.totalPOAmount = totalPOAmount;
    }

    public Double getTotalGSTAmount() {
        return this.totalGSTAmount;
    }

    public PurchaseQuotation totalGSTAmount(Double totalGSTAmount) {
        this.setTotalGSTAmount(totalGSTAmount);
        return this;
    }

    public void setTotalGSTAmount(Double totalGSTAmount) {
        this.totalGSTAmount = totalGSTAmount;
    }

    public Instant getPoDate() {
        return this.poDate;
    }

    public PurchaseQuotation poDate(Instant poDate) {
        this.setPoDate(poDate);
        return this;
    }

    public void setPoDate(Instant poDate) {
        this.poDate = poDate;
    }

    public Instant getExpectedDeliveryDate() {
        return this.expectedDeliveryDate;
    }

    public PurchaseQuotation expectedDeliveryDate(Instant expectedDeliveryDate) {
        this.setExpectedDeliveryDate(expectedDeliveryDate);
        return this;
    }

    public void setExpectedDeliveryDate(Instant expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Status getOrderStatus() {
        return this.orderStatus;
    }

    public PurchaseQuotation orderStatus(Status orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<PurchaseQuotationDetails> getPurchaseQuotationDetails() {
        return this.purchaseQuotationDetails;
    }

    public void setPurchaseQuotationDetails(Set<PurchaseQuotationDetails> purchaseQuotationDetails) {
        if (this.purchaseQuotationDetails != null) {
            this.purchaseQuotationDetails.forEach(i -> i.setPurchaseQuotation(null));
        }
        if (purchaseQuotationDetails != null) {
            purchaseQuotationDetails.forEach(i -> i.setPurchaseQuotation(this));
        }
        this.purchaseQuotationDetails = purchaseQuotationDetails;
    }

    public PurchaseQuotation purchaseQuotationDetails(Set<PurchaseQuotationDetails> purchaseQuotationDetails) {
        this.setPurchaseQuotationDetails(purchaseQuotationDetails);
        return this;
    }

    public PurchaseQuotation addPurchaseQuotationDetails(PurchaseQuotationDetails purchaseQuotationDetails) {
        this.purchaseQuotationDetails.add(purchaseQuotationDetails);
        purchaseQuotationDetails.setPurchaseQuotation(this);
        return this;
    }

    public PurchaseQuotation removePurchaseQuotationDetails(PurchaseQuotationDetails purchaseQuotationDetails) {
        this.purchaseQuotationDetails.remove(purchaseQuotationDetails);
        purchaseQuotationDetails.setPurchaseQuotation(null);
        return this;
    }

    public Set<OrderRecieved> getOrderRecieveds() {
        return this.orderRecieveds;
    }

    public void setOrderRecieveds(Set<OrderRecieved> orderRecieveds) {
        if (this.orderRecieveds != null) {
            this.orderRecieveds.forEach(i -> i.setPurchaseQuotation(null));
        }
        if (orderRecieveds != null) {
            orderRecieveds.forEach(i -> i.setPurchaseQuotation(this));
        }
        this.orderRecieveds = orderRecieveds;
    }

    public PurchaseQuotation orderRecieveds(Set<OrderRecieved> orderRecieveds) {
        this.setOrderRecieveds(orderRecieveds);
        return this;
    }

    public PurchaseQuotation addOrderRecieved(OrderRecieved orderRecieved) {
        this.orderRecieveds.add(orderRecieved);
        orderRecieved.setPurchaseQuotation(this);
        return this;
    }

    public PurchaseQuotation removeOrderRecieved(OrderRecieved orderRecieved) {
        this.orderRecieveds.remove(orderRecieved);
        orderRecieved.setPurchaseQuotation(null);
        return this;
    }

    public Clients getClients() {
        return this.clients;
    }

    public void setClients(Clients clients) {
        this.clients = clients;
    }

    public PurchaseQuotation clients(Clients clients) {
        this.setClients(clients);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseQuotation)) {
            return false;
        }
        return id != null && id.equals(((PurchaseQuotation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseQuotation{" +
            "id=" + getId() +
            ", referenceNumber='" + getReferenceNumber() + "'" +
            ", totalPOAmount=" + getTotalPOAmount() +
            ", totalGSTAmount=" + getTotalGSTAmount() +
            ", poDate='" + getPoDate() + "'" +
            ", expectedDeliveryDate='" + getExpectedDeliveryDate() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            "}";
    }
}
