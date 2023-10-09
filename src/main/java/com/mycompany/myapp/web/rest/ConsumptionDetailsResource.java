package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ConsumptionDetails;
import com.mycompany.myapp.repository.ConsumptionDetailsRepository;
import com.mycompany.myapp.service.ConsumptionDetailsService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ConsumptionDetails}.
 */
@RestController
@RequestMapping("/api")
public class ConsumptionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ConsumptionDetailsResource.class);

    private static final String ENTITY_NAME = "consumptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsumptionDetailsService consumptionDetailsService;

    private final ConsumptionDetailsRepository consumptionDetailsRepository;

    public ConsumptionDetailsResource(
        ConsumptionDetailsService consumptionDetailsService,
        ConsumptionDetailsRepository consumptionDetailsRepository
    ) {
        this.consumptionDetailsService = consumptionDetailsService;
        this.consumptionDetailsRepository = consumptionDetailsRepository;
    }

    /**
     * {@code POST  /consumption-details} : Create a new consumptionDetails.
     *
     * @param consumptionDetails the consumptionDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consumptionDetails, or with status {@code 400 (Bad Request)} if the consumptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consumption-details")
    public ResponseEntity<ConsumptionDetails> createConsumptionDetails(@RequestBody ConsumptionDetails consumptionDetails)
        throws URISyntaxException {
        log.debug("REST request to save ConsumptionDetails : {}", consumptionDetails);
        if (consumptionDetails.getId() != null) {
            throw new BadRequestAlertException("A new consumptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsumptionDetails result = consumptionDetailsService.save(consumptionDetails);
        return ResponseEntity
            .created(new URI("/api/consumption-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consumption-details/:id} : Updates an existing consumptionDetails.
     *
     * @param id the id of the consumptionDetails to save.
     * @param consumptionDetails the consumptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumptionDetails,
     * or with status {@code 400 (Bad Request)} if the consumptionDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consumptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consumption-details/{id}")
    public ResponseEntity<ConsumptionDetails> updateConsumptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsumptionDetails consumptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to update ConsumptionDetails : {}, {}", id, consumptionDetails);
        if (consumptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConsumptionDetails result = consumptionDetailsService.update(consumptionDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consumptionDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consumption-details/:id} : Partial updates given fields of an existing consumptionDetails, field will ignore if it is null
     *
     * @param id the id of the consumptionDetails to save.
     * @param consumptionDetails the consumptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumptionDetails,
     * or with status {@code 400 (Bad Request)} if the consumptionDetails is not valid,
     * or with status {@code 404 (Not Found)} if the consumptionDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the consumptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consumption-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsumptionDetails> partialUpdateConsumptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsumptionDetails consumptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConsumptionDetails partially : {}, {}", id, consumptionDetails);
        if (consumptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumptionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsumptionDetails> result = consumptionDetailsService.partialUpdate(consumptionDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, consumptionDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /consumption-details} : get all the consumptionDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consumptionDetails in body.
     */
    @GetMapping("/consumption-details")
    public ResponseEntity<List<ConsumptionDetails>> getAllConsumptionDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ConsumptionDetails");
        Page<ConsumptionDetails> page = consumptionDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consumption-details/:id} : get the "id" consumptionDetails.
     *
     * @param id the id of the consumptionDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consumptionDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consumption-details/{id}")
    public ResponseEntity<ConsumptionDetails> getConsumptionDetails(@PathVariable Long id) {
        log.debug("REST request to get ConsumptionDetails : {}", id);
        Optional<ConsumptionDetails> consumptionDetails = consumptionDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consumptionDetails);
    }

    /**
     * {@code DELETE  /consumption-details/:id} : delete the "id" consumptionDetails.
     *
     * @param id the id of the consumptionDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consumption-details/{id}")
    public ResponseEntity<Void> deleteConsumptionDetails(@PathVariable Long id) {
        log.debug("REST request to delete ConsumptionDetails : {}", id);
        consumptionDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
