package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SalesOrderd;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SalesOrderd entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesOrderdRepository extends JpaRepository<SalesOrderd, Long> {}
