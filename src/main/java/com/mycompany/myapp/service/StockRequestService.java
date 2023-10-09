package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.StockRequest;
import com.mycompany.myapp.repository.StockRequestRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StockRequest}.
 */
@Service
@Transactional
public class StockRequestService {

    private final Logger log = LoggerFactory.getLogger(StockRequestService.class);

    private final StockRequestRepository stockRequestRepository;

    public StockRequestService(StockRequestRepository stockRequestRepository) {
        this.stockRequestRepository = stockRequestRepository;
    }

    /**
     * Save a stockRequest.
     *
     * @param stockRequest the entity to save.
     * @return the persisted entity.
     */
    public StockRequest save(StockRequest stockRequest) {
        log.debug("Request to save StockRequest : {}", stockRequest);
        return stockRequestRepository.save(stockRequest);
    }

    /**
     * Update a stockRequest.
     *
     * @param stockRequest the entity to save.
     * @return the persisted entity.
     */
    public StockRequest update(StockRequest stockRequest) {
        log.debug("Request to update StockRequest : {}", stockRequest);
        return stockRequestRepository.save(stockRequest);
    }

    /**
     * Partially update a stockRequest.
     *
     * @param stockRequest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StockRequest> partialUpdate(StockRequest stockRequest) {
        log.debug("Request to partially update StockRequest : {}", stockRequest);

        return stockRequestRepository
            .findById(stockRequest.getId())
            .map(existingStockRequest -> {
                if (stockRequest.getQtyRequired() != null) {
                    existingStockRequest.setQtyRequired(stockRequest.getQtyRequired());
                }
                if (stockRequest.getReqDate() != null) {
                    existingStockRequest.setReqDate(stockRequest.getReqDate());
                }
                if (stockRequest.getIsProd() != null) {
                    existingStockRequest.setIsProd(stockRequest.getIsProd());
                }
                if (stockRequest.getStatus() != null) {
                    existingStockRequest.setStatus(stockRequest.getStatus());
                }

                return existingStockRequest;
            })
            .map(stockRequestRepository::save);
    }

    /**
     * Get all the stockRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StockRequest> findAll(Pageable pageable) {
        log.debug("Request to get all StockRequests");
        return stockRequestRepository.findAll(pageable);
    }

    /**
     * Get one stockRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StockRequest> findOne(Long id) {
        log.debug("Request to get StockRequest : {}", id);
        return stockRequestRepository.findById(id);
    }

    /**
     * Delete the stockRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StockRequest : {}", id);
        stockRequestRepository.deleteById(id);
    }
}
