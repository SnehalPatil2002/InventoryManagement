package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ConsumptionDetails;
import com.mycompany.myapp.repository.ConsumptionDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConsumptionDetails}.
 */
@Service
@Transactional
public class ConsumptionDetailsService {

    private final Logger log = LoggerFactory.getLogger(ConsumptionDetailsService.class);

    private final ConsumptionDetailsRepository consumptionDetailsRepository;

    public ConsumptionDetailsService(ConsumptionDetailsRepository consumptionDetailsRepository) {
        this.consumptionDetailsRepository = consumptionDetailsRepository;
    }

    /**
     * Save a consumptionDetails.
     *
     * @param consumptionDetails the entity to save.
     * @return the persisted entity.
     */
    public ConsumptionDetails save(ConsumptionDetails consumptionDetails) {
        log.debug("Request to save ConsumptionDetails : {}", consumptionDetails);
        return consumptionDetailsRepository.save(consumptionDetails);
    }

    /**
     * Update a consumptionDetails.
     *
     * @param consumptionDetails the entity to save.
     * @return the persisted entity.
     */
    public ConsumptionDetails update(ConsumptionDetails consumptionDetails) {
        log.debug("Request to update ConsumptionDetails : {}", consumptionDetails);
        return consumptionDetailsRepository.save(consumptionDetails);
    }

    /**
     * Partially update a consumptionDetails.
     *
     * @param consumptionDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsumptionDetails> partialUpdate(ConsumptionDetails consumptionDetails) {
        log.debug("Request to partially update ConsumptionDetails : {}", consumptionDetails);

        return consumptionDetailsRepository
            .findById(consumptionDetails.getId())
            .map(existingConsumptionDetails -> {
                if (consumptionDetails.getQuantityConsumed() != null) {
                    existingConsumptionDetails.setQuantityConsumed(consumptionDetails.getQuantityConsumed());
                }
                if (consumptionDetails.getScrapGenerated() != null) {
                    existingConsumptionDetails.setScrapGenerated(consumptionDetails.getScrapGenerated());
                }

                return existingConsumptionDetails;
            })
            .map(consumptionDetailsRepository::save);
    }

    /**
     * Get all the consumptionDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsumptionDetails> findAll(Pageable pageable) {
        log.debug("Request to get all ConsumptionDetails");
        return consumptionDetailsRepository.findAll(pageable);
    }

    /**
     * Get one consumptionDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsumptionDetails> findOne(Long id) {
        log.debug("Request to get ConsumptionDetails : {}", id);
        return consumptionDetailsRepository.findById(id);
    }

    /**
     * Delete the consumptionDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConsumptionDetails : {}", id);
        consumptionDetailsRepository.deleteById(id);
    }
}
