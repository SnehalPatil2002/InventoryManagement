package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Projects;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Projects entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long> {}
