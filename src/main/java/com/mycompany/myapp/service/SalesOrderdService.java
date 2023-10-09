package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SalesOrderd;
import com.mycompany.myapp.repository.SalesOrderdRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SalesOrderd}.
 */
@Service
@Transactional
public class SalesOrderdService {

    private final Logger log = LoggerFactory.getLogger(SalesOrderdService.class);

    private final SalesOrderdRepository salesOrderdRepository;

    public SalesOrderdService(SalesOrderdRepository salesOrderdRepository) {
        this.salesOrderdRepository = salesOrderdRepository;
    }

    /**
     * Save a salesOrderd.
     *
     * @param salesOrderd the entity to save.
     * @return the persisted entity.
     */
    public SalesOrderd save(SalesOrderd salesOrderd) {
        log.debug("Request to save SalesOrderd : {}", salesOrderd);
        return salesOrderdRepository.save(salesOrderd);
    }

    /**
     * Update a salesOrderd.
     *
     * @param salesOrderd the entity to save.
     * @return the persisted entity.
     */
    public SalesOrderd update(SalesOrderd salesOrderd) {
        log.debug("Request to update SalesOrderd : {}", salesOrderd);
        return salesOrderdRepository.save(salesOrderd);
    }

    /**
     * Partially update a salesOrderd.
     *
     * @param salesOrderd the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalesOrderd> partialUpdate(SalesOrderd salesOrderd) {
        log.debug("Request to partially update SalesOrderd : {}", salesOrderd);

        return salesOrderdRepository
            .findById(salesOrderd.getId())
            .map(existingSalesOrderd -> {
                if (salesOrderd.getOrderDate() != null) {
                    existingSalesOrderd.setOrderDate(salesOrderd.getOrderDate());
                }
                if (salesOrderd.getQuantitySold() != null) {
                    existingSalesOrderd.setQuantitySold(salesOrderd.getQuantitySold());
                }
                if (salesOrderd.getUnitPrice() != null) {
                    existingSalesOrderd.setUnitPrice(salesOrderd.getUnitPrice());
                }
                if (salesOrderd.getGstPercentage() != null) {
                    existingSalesOrderd.setGstPercentage(salesOrderd.getGstPercentage());
                }
                if (salesOrderd.getTotalRevenue() != null) {
                    existingSalesOrderd.setTotalRevenue(salesOrderd.getTotalRevenue());
                }
                if (salesOrderd.getStatus() != null) {
                    existingSalesOrderd.setStatus(salesOrderd.getStatus());
                }

                return existingSalesOrderd;
            })
            .map(salesOrderdRepository::save);
    }

    /**
     * Get all the salesOrderds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SalesOrderd> findAll(Pageable pageable) {
        log.debug("Request to get all SalesOrderds");
        return salesOrderdRepository.findAll(pageable);
    }

    /**
     * Get one salesOrderd by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalesOrderd> findOne(Long id) {
        log.debug("Request to get SalesOrderd : {}", id);
        return salesOrderdRepository.findById(id);
    }

    /**
     * Delete the salesOrderd by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SalesOrderd : {}", id);
        salesOrderdRepository.deleteById(id);
    }
}
