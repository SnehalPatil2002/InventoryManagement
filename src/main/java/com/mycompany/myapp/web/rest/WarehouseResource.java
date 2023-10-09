package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Warehouse;
import com.mycompany.myapp.repository.WarehouseRepository;
import com.mycompany.myapp.service.WarehouseService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Warehouse}.
 */
@RestController
@RequestMapping("/api")
public class WarehouseResource {

    private final Logger log = LoggerFactory.getLogger(WarehouseResource.class);

    private static final String ENTITY_NAME = "warehouse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarehouseService warehouseService;

    private final WarehouseRepository warehouseRepository;

    public WarehouseResource(WarehouseService warehouseService, WarehouseRepository warehouseRepository) {
        this.warehouseService = warehouseService;
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * {@code POST  /warehouses} : Create a new warehouse.
     *
     * @param warehouse the warehouse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warehouse, or with status {@code 400 (Bad Request)} if the warehouse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/warehouses")
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) throws URISyntaxException {
        log.debug("REST request to save Warehouse : {}", warehouse);
        if (warehouse.getId() != null) {
            throw new BadRequestAlertException("A new warehouse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Warehouse result = warehouseService.save(warehouse);
        return ResponseEntity
            .created(new URI("/api/warehouses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /warehouses/:id} : Updates an existing warehouse.
     *
     * @param id the id of the warehouse to save.
     * @param warehouse the warehouse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouse,
     * or with status {@code 400 (Bad Request)} if the warehouse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warehouse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/warehouses/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Warehouse warehouse
    ) throws URISyntaxException {
        log.debug("REST request to update Warehouse : {}, {}", id, warehouse);
        if (warehouse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warehouseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Warehouse result = warehouseService.update(warehouse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, warehouse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /warehouses/:id} : Partial updates given fields of an existing warehouse, field will ignore if it is null
     *
     * @param id the id of the warehouse to save.
     * @param warehouse the warehouse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouse,
     * or with status {@code 400 (Bad Request)} if the warehouse is not valid,
     * or with status {@code 404 (Not Found)} if the warehouse is not found,
     * or with status {@code 500 (Internal Server Error)} if the warehouse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/warehouses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Warehouse> partialUpdateWarehouse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Warehouse warehouse
    ) throws URISyntaxException {
        log.debug("REST request to partial update Warehouse partially : {}, {}", id, warehouse);
        if (warehouse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warehouseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Warehouse> result = warehouseService.partialUpdate(warehouse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, warehouse.getId().toString())
        );
    }

    /**
     * {@code GET  /warehouses} : get all the warehouses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warehouses in body.
     */
    @GetMapping("/warehouses")
    public ResponseEntity<List<Warehouse>> getAllWarehouses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Warehouses");
        Page<Warehouse> page = warehouseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /warehouses/:id} : get the "id" warehouse.
     *
     * @param id the id of the warehouse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warehouse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/warehouses/{id}")
    public ResponseEntity<Warehouse> getWarehouse(@PathVariable Long id) {
        log.debug("REST request to get Warehouse : {}", id);
        Optional<Warehouse> warehouse = warehouseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warehouse);
    }

    /**
     * {@code DELETE  /warehouses/:id} : delete the "id" warehouse.
     *
     * @param id the id of the warehouse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/warehouses/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        log.debug("REST request to delete Warehouse : {}", id);
        warehouseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
