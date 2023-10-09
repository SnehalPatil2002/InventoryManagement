package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Projects;
import com.mycompany.myapp.repository.ProjectsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Projects}.
 */
@Service
@Transactional
public class ProjectsService {

    private final Logger log = LoggerFactory.getLogger(ProjectsService.class);

    private final ProjectsRepository projectsRepository;

    public ProjectsService(ProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    /**
     * Save a projects.
     *
     * @param projects the entity to save.
     * @return the persisted entity.
     */
    public Projects save(Projects projects) {
        log.debug("Request to save Projects : {}", projects);
        return projectsRepository.save(projects);
    }

    /**
     * Update a projects.
     *
     * @param projects the entity to save.
     * @return the persisted entity.
     */
    public Projects update(Projects projects) {
        log.debug("Request to update Projects : {}", projects);
        return projectsRepository.save(projects);
    }

    /**
     * Partially update a projects.
     *
     * @param projects the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Projects> partialUpdate(Projects projects) {
        log.debug("Request to partially update Projects : {}", projects);

        return projectsRepository
            .findById(projects.getId())
            .map(existingProjects -> {
                if (projects.getProjectName() != null) {
                    existingProjects.setProjectName(projects.getProjectName());
                }
                if (projects.getStartDate() != null) {
                    existingProjects.setStartDate(projects.getStartDate());
                }
                if (projects.getEndDate() != null) {
                    existingProjects.setEndDate(projects.getEndDate());
                }
                if (projects.getOrderQuantity() != null) {
                    existingProjects.setOrderQuantity(projects.getOrderQuantity());
                }
                if (projects.getFinalTotal() != null) {
                    existingProjects.setFinalTotal(projects.getFinalTotal());
                }

                return existingProjects;
            })
            .map(projectsRepository::save);
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Projects> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectsRepository.findAll(pageable);
    }

    /**
     * Get one projects by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Projects> findOne(Long id) {
        log.debug("Request to get Projects : {}", id);
        return projectsRepository.findById(id);
    }

    /**
     * Delete the projects by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Projects : {}", id);
        projectsRepository.deleteById(id);
    }
}
