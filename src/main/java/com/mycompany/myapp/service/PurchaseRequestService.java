package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PurchaseRequest;
import com.mycompany.myapp.repository.PurchaseRequestRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchaseRequest}.
 */
@Service
@Transactional
public class PurchaseRequestService {

    private final Logger log = LoggerFactory.getLogger(PurchaseRequestService.class);

    private final PurchaseRequestRepository purchaseRequestRepository;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    /**
     * Save a purchaseRequest.
     *
     * @param purchaseRequest the entity to save.
     * @return the persisted entity.
     */
    public PurchaseRequest save(PurchaseRequest purchaseRequest) {
        log.debug("Request to save PurchaseRequest : {}", purchaseRequest);
        return purchaseRequestRepository.save(purchaseRequest);
    }

    /**
     * Update a purchaseRequest.
     *
     * @param purchaseRequest the entity to save.
     * @return the persisted entity.
     */
    public PurchaseRequest update(PurchaseRequest purchaseRequest) {
        log.debug("Request to update PurchaseRequest : {}", purchaseRequest);
        return purchaseRequestRepository.save(purchaseRequest);
    }

    /**
     * Partially update a purchaseRequest.
     *
     * @param purchaseRequest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PurchaseRequest> partialUpdate(PurchaseRequest purchaseRequest) {
        log.debug("Request to partially update PurchaseRequest : {}", purchaseRequest);

        return purchaseRequestRepository
            .findById(purchaseRequest.getId())
            .map(existingPurchaseRequest -> {
                if (purchaseRequest.getQtyRequired() != null) {
                    existingPurchaseRequest.setQtyRequired(purchaseRequest.getQtyRequired());
                }
                if (purchaseRequest.getRequestDate() != null) {
                    existingPurchaseRequest.setRequestDate(purchaseRequest.getRequestDate());
                }
                if (purchaseRequest.getExpectedDate() != null) {
                    existingPurchaseRequest.setExpectedDate(purchaseRequest.getExpectedDate());
                }
                if (purchaseRequest.getStatus() != null) {
                    existingPurchaseRequest.setStatus(purchaseRequest.getStatus());
                }

                return existingPurchaseRequest;
            })
            .map(purchaseRequestRepository::save);
    }

    /**
     * Get all the purchaseRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseRequest> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseRequests");
        return purchaseRequestRepository.findAll(pageable);
    }

    /**
     * Get one purchaseRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseRequest> findOne(Long id) {
        log.debug("Request to get PurchaseRequest : {}", id);
        return purchaseRequestRepository.findById(id);
    }

    /**
     * Delete the purchaseRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseRequest : {}", id);
        purchaseRequestRepository.deleteById(id);
    }
}
