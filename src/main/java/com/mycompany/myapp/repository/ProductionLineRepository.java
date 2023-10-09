package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductionLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductionLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, Long> {}
