package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PurchaseQuotationDetails;
import com.mycompany.myapp.repository.PurchaseQuotationDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchaseQuotationDetails}.
 */
@Service
@Transactional
public class PurchaseQuotationDetailsService {

    private final Logger log = LoggerFactory.getLogger(PurchaseQuotationDetailsService.class);

    private final PurchaseQuotationDetailsRepository purchaseQuotationDetailsRepository;

    public PurchaseQuotationDetailsService(PurchaseQuotationDetailsRepository purchaseQuotationDetailsRepository) {
        this.purchaseQuotationDetailsRepository = purchaseQuotationDetailsRepository;
    }

    /**
     * Save a purchaseQuotationDetails.
     *
     * @param purchaseQuotationDetails the entity to save.
     * @return the persisted entity.
     */
    public PurchaseQuotationDetails save(PurchaseQuotationDetails purchaseQuotationDetails) {
        log.debug("Request to save PurchaseQuotationDetails : {}", purchaseQuotationDetails);
        return purchaseQuotationDetailsRepository.save(purchaseQuotationDetails);
    }

    /**
     * Update a purchaseQuotationDetails.
     *
     * @param purchaseQuotationDetails the entity to save.
     * @return the persisted entity.
     */
    public PurchaseQuotationDetails update(PurchaseQuotationDetails purchaseQuotationDetails) {
        log.debug("Request to update PurchaseQuotationDetails : {}", purchaseQuotationDetails);
        return purchaseQuotationDetailsRepository.save(purchaseQuotationDetails);
    }

    /**
     * Partially update a purchaseQuotationDetails.
     *
     * @param purchaseQuotationDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PurchaseQuotationDetails> partialUpdate(PurchaseQuotationDetails purchaseQuotationDetails) {
        log.debug("Request to partially update PurchaseQuotationDetails : {}", purchaseQuotationDetails);

        return purchaseQuotationDetailsRepository
            .findById(purchaseQuotationDetails.getId())
            .map(existingPurchaseQuotationDetails -> {
                if (purchaseQuotationDetails.getQtyOrdered() != null) {
                    existingPurchaseQuotationDetails.setQtyOrdered(purchaseQuotationDetails.getQtyOrdered());
                }
                if (purchaseQuotationDetails.getGstTaxPercentage() != null) {
                    existingPurchaseQuotationDetails.setGstTaxPercentage(purchaseQuotationDetails.getGstTaxPercentage());
                }
                if (purchaseQuotationDetails.getPricePerUnit() != null) {
                    existingPurchaseQuotationDetails.setPricePerUnit(purchaseQuotationDetails.getPricePerUnit());
                }
                if (purchaseQuotationDetails.getTotalPrice() != null) {
                    existingPurchaseQuotationDetails.setTotalPrice(purchaseQuotationDetails.getTotalPrice());
                }
                if (purchaseQuotationDetails.getDiscount() != null) {
                    existingPurchaseQuotationDetails.setDiscount(purchaseQuotationDetails.getDiscount());
                }

                return existingPurchaseQuotationDetails;
            })
            .map(purchaseQuotationDetailsRepository::save);
    }

    /**
     * Get all the purchaseQuotationDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseQuotationDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseQuotationDetails");
        return purchaseQuotationDetailsRepository.findAll(pageable);
    }

    /**
     * Get one purchaseQuotationDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseQuotationDetails> findOne(Long id) {
        log.debug("Request to get PurchaseQuotationDetails : {}", id);
        return purchaseQuotationDetailsRepository.findById(id);
    }

    /**
     * Delete the purchaseQuotationDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseQuotationDetails : {}", id);
        purchaseQuotationDetailsRepository.deleteById(id);
    }
}
