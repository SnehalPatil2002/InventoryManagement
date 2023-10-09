package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductPrice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {}
