package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A PurchaseRequest.
 */
@Entity
@Table(name = "purchase_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qty_required")
    private Long qtyRequired;

    @Column(name = "request_date")
    private Instant requestDate;

    @Column(name = "expected_date")
    private Instant expectedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @JsonIgnoreProperties(value = { "productRawMaterials", "warehouse" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private RawMaterial rawMaterial;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQtyRequired() {
        return this.qtyRequired;
    }

    public PurchaseRequest qtyRequired(Long qtyRequired) {
        this.setQtyRequired(qtyRequired);
        return this;
    }

    public void setQtyRequired(Long qtyRequired) {
        this.qtyRequired = qtyRequired;
    }

    public Instant getRequestDate() {
        return this.requestDate;
    }

    public PurchaseRequest requestDate(Instant requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public Instant getExpectedDate() {
        return this.expectedDate;
    }

    public PurchaseRequest expectedDate(Instant expectedDate) {
        this.setExpectedDate(expectedDate);
        return this;
    }

    public void setExpectedDate(Instant expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public PurchaseRequest status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public RawMaterial getRawMaterial() {
        return this.rawMaterial;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public PurchaseRequest rawMaterial(RawMaterial rawMaterial) {
        this.setRawMaterial(rawMaterial);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseRequest)) {
            return false;
        }
        return id != null && id.equals(((PurchaseRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseRequest{" +
            "id=" + getId() +
            ", qtyRequired=" + getQtyRequired() +
            ", requestDate='" + getRequestDate() + "'" +
            ", expectedDate='" + getExpectedDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
