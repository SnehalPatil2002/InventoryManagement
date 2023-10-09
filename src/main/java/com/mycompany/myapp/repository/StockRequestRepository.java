package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StockRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRequestRepository extends JpaRepository<StockRequest, Long> {}
