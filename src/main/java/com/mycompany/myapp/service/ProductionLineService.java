package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ProductionLine;
import com.mycompany.myapp.repository.ProductionLineRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductionLine}.
 */
@Service
@Transactional
public class ProductionLineService {

    private final Logger log = LoggerFactory.getLogger(ProductionLineService.class);

    private final ProductionLineRepository productionLineRepository;

    public ProductionLineService(ProductionLineRepository productionLineRepository) {
        this.productionLineRepository = productionLineRepository;
    }

    /**
     * Save a productionLine.
     *
     * @param productionLine the entity to save.
     * @return the persisted entity.
     */
    public ProductionLine save(ProductionLine productionLine) {
        log.debug("Request to save ProductionLine : {}", productionLine);
        return productionLineRepository.save(productionLine);
    }

    /**
     * Update a productionLine.
     *
     * @param productionLine the entity to save.
     * @return the persisted entity.
     */
    public ProductionLine update(ProductionLine productionLine) {
        log.debug("Request to update ProductionLine : {}", productionLine);
        return productionLineRepository.save(productionLine);
    }

    /**
     * Partially update a productionLine.
     *
     * @param productionLine the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductionLine> partialUpdate(ProductionLine productionLine) {
        log.debug("Request to partially update ProductionLine : {}", productionLine);

        return productionLineRepository
            .findById(productionLine.getId())
            .map(existingProductionLine -> {
                if (productionLine.getDescription() != null) {
                    existingProductionLine.setDescription(productionLine.getDescription());
                }
                if (productionLine.getIsActive() != null) {
                    existingProductionLine.setIsActive(productionLine.getIsActive());
                }

                return existingProductionLine;
            })
            .map(productionLineRepository::save);
    }

    /**
     * Get all the productionLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductionLine> findAll(Pageable pageable) {
        log.debug("Request to get all ProductionLines");
        return productionLineRepository.findAll(pageable);
    }

    /**
     * Get one productionLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductionLine> findOne(Long id) {
        log.debug("Request to get ProductionLine : {}", id);
        return productionLineRepository.findById(id);
    }

    /**
     * Delete the productionLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductionLine : {}", id);
        productionLineRepository.deleteById(id);
    }
}
