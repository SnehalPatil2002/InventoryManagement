package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.OrderRecieved;
import com.mycompany.myapp.repository.OrderRecievedRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderRecieved}.
 */
@Service
@Transactional
public class OrderRecievedService {

    private final Logger log = LoggerFactory.getLogger(OrderRecievedService.class);

    private final OrderRecievedRepository orderRecievedRepository;

    public OrderRecievedService(OrderRecievedRepository orderRecievedRepository) {
        this.orderRecievedRepository = orderRecievedRepository;
    }

    /**
     * Save a orderRecieved.
     *
     * @param orderRecieved the entity to save.
     * @return the persisted entity.
     */
    public OrderRecieved save(OrderRecieved orderRecieved) {
        log.debug("Request to save OrderRecieved : {}", orderRecieved);
        return orderRecievedRepository.save(orderRecieved);
    }

    /**
     * Update a orderRecieved.
     *
     * @param orderRecieved the entity to save.
     * @return the persisted entity.
     */
    public OrderRecieved update(OrderRecieved orderRecieved) {
        log.debug("Request to update OrderRecieved : {}", orderRecieved);
        return orderRecievedRepository.save(orderRecieved);
    }

    /**
     * Partially update a orderRecieved.
     *
     * @param orderRecieved the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderRecieved> partialUpdate(OrderRecieved orderRecieved) {
        log.debug("Request to partially update OrderRecieved : {}", orderRecieved);

        return orderRecievedRepository
            .findById(orderRecieved.getId())
            .map(existingOrderRecieved -> {
                if (orderRecieved.getReferenceNumber() != null) {
                    existingOrderRecieved.setReferenceNumber(orderRecieved.getReferenceNumber());
                }
                if (orderRecieved.getOrDate() != null) {
                    existingOrderRecieved.setOrDate(orderRecieved.getOrDate());
                }
                if (orderRecieved.getQtyOrdered() != null) {
                    existingOrderRecieved.setQtyOrdered(orderRecieved.getQtyOrdered());
                }
                if (orderRecieved.getQtyRecieved() != null) {
                    existingOrderRecieved.setQtyRecieved(orderRecieved.getQtyRecieved());
                }
                if (orderRecieved.getManufacturingDate() != null) {
                    existingOrderRecieved.setManufacturingDate(orderRecieved.getManufacturingDate());
                }
                if (orderRecieved.getExpiryDate() != null) {
                    existingOrderRecieved.setExpiryDate(orderRecieved.getExpiryDate());
                }
                if (orderRecieved.getQtyApproved() != null) {
                    existingOrderRecieved.setQtyApproved(orderRecieved.getQtyApproved());
                }
                if (orderRecieved.getQtyRejected() != null) {
                    existingOrderRecieved.setQtyRejected(orderRecieved.getQtyRejected());
                }

                return existingOrderRecieved;
            })
            .map(orderRecievedRepository::save);
    }

    /**
     * Get all the orderRecieveds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderRecieved> findAll(Pageable pageable) {
        log.debug("Request to get all OrderRecieveds");
        return orderRecievedRepository.findAll(pageable);
    }

    /**
     * Get one orderRecieved by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderRecieved> findOne(Long id) {
        log.debug("Request to get OrderRecieved : {}", id);
        return orderRecievedRepository.findById(id);
    }

    /**
     * Delete the orderRecieved by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderRecieved : {}", id);
        orderRecievedRepository.deleteById(id);
    }
}
