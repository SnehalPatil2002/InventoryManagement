package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PurchaseQuotation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchaseQuotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseQuotationRepository extends JpaRepository<PurchaseQuotation, Long> {}
