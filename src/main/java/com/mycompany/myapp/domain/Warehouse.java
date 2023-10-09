package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Warehouse.
 */
@Entity
@Table(name = "warehouse")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "wh_name")
    private String whName;

    @Column(name = "address")
    private String address;

    @Column(name = "pincode")
    private Long pincode;

    @Column(name = "city")
    private String city;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "manager_email")
    private String managerEmail;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = { "productRawMaterials", "warehouse" }, allowSetters = true)
    private Set<RawMaterial> rawMaterials = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Warehouse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhName() {
        return this.whName;
    }

    public Warehouse whName(String whName) {
        this.setWhName(whName);
        return this;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getAddress() {
        return this.address;
    }

    public Warehouse address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPincode() {
        return this.pincode;
    }

    public Warehouse pincode(Long pincode) {
        this.setPincode(pincode);
        return this;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return this.city;
    }

    public Warehouse city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public Warehouse managerName(String managerName) {
        this.setManagerName(managerName);
        return this;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return this.managerEmail;
    }

    public Warehouse managerEmail(String managerEmail) {
        this.setManagerEmail(managerEmail);
        return this;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public Set<RawMaterial> getRawMaterials() {
        return this.rawMaterials;
    }

    public void setRawMaterials(Set<RawMaterial> rawMaterials) {
        if (this.rawMaterials != null) {
            this.rawMaterials.forEach(i -> i.setWarehouse(null));
        }
        if (rawMaterials != null) {
            rawMaterials.forEach(i -> i.setWarehouse(this));
        }
        this.rawMaterials = rawMaterials;
    }

    public Warehouse rawMaterials(Set<RawMaterial> rawMaterials) {
        this.setRawMaterials(rawMaterials);
        return this;
    }

    public Warehouse addRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterials.add(rawMaterial);
        rawMaterial.setWarehouse(this);
        return this;
    }

    public Warehouse removeRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterials.remove(rawMaterial);
        rawMaterial.setWarehouse(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse)) {
            return false;
        }
        return id != null && id.equals(((Warehouse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Warehouse{" +
            "id=" + getId() +
            ", whName='" + getWhName() + "'" +
            ", address='" + getAddress() + "'" +
            ", pincode=" + getPincode() +
            ", city='" + getCity() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", managerEmail='" + getManagerEmail() + "'" +
            "}";
    }
}
