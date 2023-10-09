package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PurchaseQuotation;
import com.mycompany.myapp.repository.PurchaseQuotationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchaseQuotation}.
 */
@Service
@Transactional
public class PurchaseQuotationService {

    private final Logger log = LoggerFactory.getLogger(PurchaseQuotationService.class);

    private final PurchaseQuotationRepository purchaseQuotationRepository;

    public PurchaseQuotationService(PurchaseQuotationRepository purchaseQuotationRepository) {
        this.purchaseQuotationRepository = purchaseQuotationRepository;
    }

    /**
     * Save a purchaseQuotation.
     *
     * @param purchaseQuotation the entity to save.
     * @return the persisted entity.
     */
    public PurchaseQuotation save(PurchaseQuotation purchaseQuotation) {
        log.debug("Request to save PurchaseQuotation : {}", purchaseQuotation);
        return purchaseQuotationRepository.save(purchaseQuotation);
    }

    /**
     * Update a purchaseQuotation.
     *
     * @param purchaseQuotation the entity to save.
     * @return the persisted entity.
     */
    public PurchaseQuotation update(PurchaseQuotation purchaseQuotation) {
        log.debug("Request to update PurchaseQuotation : {}", purchaseQuotation);
        return purchaseQuotationRepository.save(purchaseQuotation);
    }

    /**
     * Partially update a purchaseQuotation.
     *
     * @param purchaseQuotation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PurchaseQuotation> partialUpdate(PurchaseQuotation purchaseQuotation) {
        log.debug("Request to partially update PurchaseQuotation : {}", purchaseQuotation);

        return purchaseQuotationRepository
            .findById(purchaseQuotation.getId())
            .map(existingPurchaseQuotation -> {
                if (purchaseQuotation.getReferenceNumber() != null) {
                    existingPurchaseQuotation.setReferenceNumber(purchaseQuotation.getReferenceNumber());
                }
                if (purchaseQuotation.getTotalPOAmount() != null) {
                    existingPurchaseQuotation.setTotalPOAmount(purchaseQuotation.getTotalPOAmount());
                }
                if (purchaseQuotation.getTotalGSTAmount() != null) {
                    existingPurchaseQuotation.setTotalGSTAmount(purchaseQuotation.getTotalGSTAmount());
                }
                if (purchaseQuotation.getPoDate() != null) {
                    existingPurchaseQuotation.setPoDate(purchaseQuotation.getPoDate());
                }
                if (purchaseQuotation.getExpectedDeliveryDate() != null) {
                    existingPurchaseQuotation.setExpectedDeliveryDate(purchaseQuotation.getExpectedDeliveryDate());
                }
                if (purchaseQuotation.getOrderStatus() != null) {
                    existingPurchaseQuotation.setOrderStatus(purchaseQuotation.getOrderStatus());
                }

                return existingPurchaseQuotation;
            })
            .map(purchaseQuotationRepository::save);
    }

    /**
     * Get all the purchaseQuotations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseQuotation> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseQuotations");
        return purchaseQuotationRepository.findAll(pageable);
    }

    /**
     * Get one purchaseQuotation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseQuotation> findOne(Long id) {
        log.debug("Request to get PurchaseQuotation : {}", id);
        return purchaseQuotationRepository.findById(id);
    }

    /**
     * Delete the purchaseQuotation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseQuotation : {}", id);
        purchaseQuotationRepository.deleteById(id);
    }
}
