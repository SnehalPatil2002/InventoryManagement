package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Warehouse;
import com.mycompany.myapp.repository.WarehouseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Warehouse}.
 */
@Service
@Transactional
public class WarehouseService {

    private final Logger log = LoggerFactory.getLogger(WarehouseService.class);

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * Save a warehouse.
     *
     * @param warehouse the entity to save.
     * @return the persisted entity.
     */
    public Warehouse save(Warehouse warehouse) {
        log.debug("Request to save Warehouse : {}", warehouse);
        return warehouseRepository.save(warehouse);
    }

    /**
     * Update a warehouse.
     *
     * @param warehouse the entity to save.
     * @return the persisted entity.
     */
    public Warehouse update(Warehouse warehouse) {
        log.debug("Request to update Warehouse : {}", warehouse);
        return warehouseRepository.save(warehouse);
    }

    /**
     * Partially update a warehouse.
     *
     * @param warehouse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Warehouse> partialUpdate(Warehouse warehouse) {
        log.debug("Request to partially update Warehouse : {}", warehouse);

        return warehouseRepository
            .findById(warehouse.getId())
            .map(existingWarehouse -> {
                if (warehouse.getWhName() != null) {
                    existingWarehouse.setWhName(warehouse.getWhName());
                }
                if (warehouse.getAddress() != null) {
                    existingWarehouse.setAddress(warehouse.getAddress());
                }
                if (warehouse.getPincode() != null) {
                    existingWarehouse.setPincode(warehouse.getPincode());
                }
                if (warehouse.getCity() != null) {
                    existingWarehouse.setCity(warehouse.getCity());
                }
                if (warehouse.getManagerName() != null) {
                    existingWarehouse.setManagerName(warehouse.getManagerName());
                }
                if (warehouse.getManagerEmail() != null) {
                    existingWarehouse.setManagerEmail(warehouse.getManagerEmail());
                }

                return existingWarehouse;
            })
            .map(warehouseRepository::save);
    }

    /**
     * Get all the warehouses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Warehouse> findAll(Pageable pageable) {
        log.debug("Request to get all Warehouses");
        return warehouseRepository.findAll(pageable);
    }

    /**
     * Get one warehouse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Warehouse> findOne(Long id) {
        log.debug("Request to get Warehouse : {}", id);
        return warehouseRepository.findById(id);
    }

    /**
     * Delete the warehouse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Warehouse : {}", id);
        warehouseRepository.deleteById(id);
    }
}
