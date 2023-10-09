package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PurchaseRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchaseRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {}
