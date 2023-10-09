package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ConsumptionDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConsumptionDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumptionDetailsRepository extends JpaRepository<ConsumptionDetails, Long> {}
