package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RawMaterial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RawMaterial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {}
