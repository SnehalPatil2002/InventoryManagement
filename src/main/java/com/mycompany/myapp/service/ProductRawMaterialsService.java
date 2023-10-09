package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ProductRawMaterials;
import com.mycompany.myapp.repository.ProductRawMaterialsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductRawMaterials}.
 */
@Service
@Transactional
public class ProductRawMaterialsService {

    private final Logger log = LoggerFactory.getLogger(ProductRawMaterialsService.class);

    private final ProductRawMaterialsRepository productRawMaterialsRepository;

    public ProductRawMaterialsService(ProductRawMaterialsRepository productRawMaterialsRepository) {
        this.productRawMaterialsRepository = productRawMaterialsRepository;
    }

    /**
     * Save a productRawMaterials.
     *
     * @param productRawMaterials the entity to save.
     * @return the persisted entity.
     */
    public ProductRawMaterials save(ProductRawMaterials productRawMaterials) {
        log.debug("Request to save ProductRawMaterials : {}", productRawMaterials);
        return productRawMaterialsRepository.save(productRawMaterials);
    }

    /**
     * Update a productRawMaterials.
     *
     * @param productRawMaterials the entity to save.
     * @return the persisted entity.
     */
    public ProductRawMaterials update(ProductRawMaterials productRawMaterials) {
        log.debug("Request to update ProductRawMaterials : {}", productRawMaterials);
        return productRawMaterialsRepository.save(productRawMaterials);
    }

    /**
     * Partially update a productRawMaterials.
     *
     * @param productRawMaterials the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductRawMaterials> partialUpdate(ProductRawMaterials productRawMaterials) {
        log.debug("Request to partially update ProductRawMaterials : {}", productRawMaterials);

        return productRawMaterialsRepository
            .findById(productRawMaterials.getId())
            .map(existingProductRawMaterials -> {
                if (productRawMaterials.getQuantityRequired() != null) {
                    existingProductRawMaterials.setQuantityRequired(productRawMaterials.getQuantityRequired());
                }

                return existingProductRawMaterials;
            })
            .map(productRawMaterialsRepository::save);
    }

    /**
     * Get all the productRawMaterials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductRawMaterials> findAll(Pageable pageable) {
        log.debug("Request to get all ProductRawMaterials");
        return productRawMaterialsRepository.findAll(pageable);
    }

    /**
     * Get one productRawMaterials by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductRawMaterials> findOne(Long id) {
        log.debug("Request to get ProductRawMaterials : {}", id);
        return productRawMaterialsRepository.findById(id);
    }

    /**
     * Delete the productRawMaterials by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductRawMaterials : {}", id);
        productRawMaterialsRepository.deleteById(id);
    }
}
