package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.RawMaterial;
import com.mycompany.myapp.repository.RawMaterialRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RawMaterial}.
 */
@Service
@Transactional
public class RawMaterialService {

    private final Logger log = LoggerFactory.getLogger(RawMaterialService.class);

    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
    }

    /**
     * Save a rawMaterial.
     *
     * @param rawMaterial the entity to save.
     * @return the persisted entity.
     */
    public RawMaterial save(RawMaterial rawMaterial) {
        log.debug("Request to save RawMaterial : {}", rawMaterial);
        return rawMaterialRepository.save(rawMaterial);
    }

    /**
     * Update a rawMaterial.
     *
     * @param rawMaterial the entity to save.
     * @return the persisted entity.
     */
    public RawMaterial update(RawMaterial rawMaterial) {
        log.debug("Request to update RawMaterial : {}", rawMaterial);
        return rawMaterialRepository.save(rawMaterial);
    }

    /**
     * Partially update a rawMaterial.
     *
     * @param rawMaterial the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RawMaterial> partialUpdate(RawMaterial rawMaterial) {
        log.debug("Request to partially update RawMaterial : {}", rawMaterial);

        return rawMaterialRepository
            .findById(rawMaterial.getId())
            .map(existingRawMaterial -> {
                if (rawMaterial.getName() != null) {
                    existingRawMaterial.setName(rawMaterial.getName());
                }
                if (rawMaterial.getBarcode() != null) {
                    existingRawMaterial.setBarcode(rawMaterial.getBarcode());
                }
                if (rawMaterial.getQuantity() != null) {
                    existingRawMaterial.setQuantity(rawMaterial.getQuantity());
                }
                if (rawMaterial.getUnitPrice() != null) {
                    existingRawMaterial.setUnitPrice(rawMaterial.getUnitPrice());
                }
                if (rawMaterial.getUnitMeasure() != null) {
                    existingRawMaterial.setUnitMeasure(rawMaterial.getUnitMeasure());
                }
                if (rawMaterial.getGstPercentage() != null) {
                    existingRawMaterial.setGstPercentage(rawMaterial.getGstPercentage());
                }
                if (rawMaterial.getReorderPoint() != null) {
                    existingRawMaterial.setReorderPoint(rawMaterial.getReorderPoint());
                }

                return existingRawMaterial;
            })
            .map(rawMaterialRepository::save);
    }

    /**
     * Get all the rawMaterials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RawMaterial> findAll(Pageable pageable) {
        log.debug("Request to get all RawMaterials");
        return rawMaterialRepository.findAll(pageable);
    }

    /**
     * Get one rawMaterial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RawMaterial> findOne(Long id) {
        log.debug("Request to get RawMaterial : {}", id);
        return rawMaterialRepository.findById(id);
    }

    /**
     * Delete the rawMaterial by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RawMaterial : {}", id);
        rawMaterialRepository.deleteById(id);
    }
}
