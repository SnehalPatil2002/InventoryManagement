package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ProductPrice;
import com.mycompany.myapp.repository.ProductPriceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductPrice}.
 */
@Service
@Transactional
public class ProductPriceService {

    private final Logger log = LoggerFactory.getLogger(ProductPriceService.class);

    private final ProductPriceRepository productPriceRepository;

    public ProductPriceService(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    /**
     * Save a productPrice.
     *
     * @param productPrice the entity to save.
     * @return the persisted entity.
     */
    public ProductPrice save(ProductPrice productPrice) {
        log.debug("Request to save ProductPrice : {}", productPrice);
        return productPriceRepository.save(productPrice);
    }

    /**
     * Update a productPrice.
     *
     * @param productPrice the entity to save.
     * @return the persisted entity.
     */
    public ProductPrice update(ProductPrice productPrice) {
        log.debug("Request to update ProductPrice : {}", productPrice);
        return productPriceRepository.save(productPrice);
    }

    /**
     * Partially update a productPrice.
     *
     * @param productPrice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductPrice> partialUpdate(ProductPrice productPrice) {
        log.debug("Request to partially update ProductPrice : {}", productPrice);

        return productPriceRepository
            .findById(productPrice.getId())
            .map(existingProductPrice -> {
                if (productPrice.getRawMaterialCost() != null) {
                    existingProductPrice.setRawMaterialCost(productPrice.getRawMaterialCost());
                }
                if (productPrice.getManufacturingCost() != null) {
                    existingProductPrice.setManufacturingCost(productPrice.getManufacturingCost());
                }
                if (productPrice.getLabourCost() != null) {
                    existingProductPrice.setLabourCost(productPrice.getLabourCost());
                }

                return existingProductPrice;
            })
            .map(productPriceRepository::save);
    }

    /**
     * Get all the productPrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductPrice> findAll(Pageable pageable) {
        log.debug("Request to get all ProductPrices");
        return productPriceRepository.findAll(pageable);
    }

    /**
     * Get one productPrice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductPrice> findOne(Long id) {
        log.debug("Request to get ProductPrice : {}", id);
        return productPriceRepository.findById(id);
    }

    /**
     * Delete the productPrice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductPrice : {}", id);
        productPriceRepository.deleteById(id);
    }
}
