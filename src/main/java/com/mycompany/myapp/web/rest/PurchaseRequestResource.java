package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PurchaseRequest;
import com.mycompany.myapp.repository.PurchaseRequestRepository;
import com.mycompany.myapp.service.PurchaseRequestService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PurchaseRequest}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseRequestResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseRequestResource.class);

    private static final String ENTITY_NAME = "purchaseRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseRequestService purchaseRequestService;

    private final PurchaseRequestRepository purchaseRequestRepository;

    public PurchaseRequestResource(PurchaseRequestService purchaseRequestService, PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseRequestService = purchaseRequestService;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    /**
     * {@code POST  /purchase-requests} : Create a new purchaseRequest.
     *
     * @param purchaseRequest the purchaseRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseRequest, or with status {@code 400 (Bad Request)} if the purchaseRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-requests")
    public ResponseEntity<PurchaseRequest> createPurchaseRequest(@RequestBody PurchaseRequest purchaseRequest) throws URISyntaxException {
        log.debug("REST request to save PurchaseRequest : {}", purchaseRequest);
        if (purchaseRequest.getId() != null) {
            throw new BadRequestAlertException("A new purchaseRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseRequest result = purchaseRequestService.save(purchaseRequest);
        return ResponseEntity
            .created(new URI("/api/purchase-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-requests/:id} : Updates an existing purchaseRequest.
     *
     * @param id the id of the purchaseRequest to save.
     * @param purchaseRequest the purchaseRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseRequest,
     * or with status {@code 400 (Bad Request)} if the purchaseRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-requests/{id}")
    public ResponseEntity<PurchaseRequest> updatePurchaseRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseRequest purchaseRequest
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseRequest : {}, {}", id, purchaseRequest);
        if (purchaseRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseRequest result = purchaseRequestService.update(purchaseRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchaseRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-requests/:id} : Partial updates given fields of an existing purchaseRequest, field will ignore if it is null
     *
     * @param id the id of the purchaseRequest to save.
     * @param purchaseRequest the purchaseRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseRequest,
     * or with status {@code 400 (Bad Request)} if the purchaseRequest is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseRequest> partialUpdatePurchaseRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseRequest purchaseRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseRequest partially : {}, {}", id, purchaseRequest);
        if (purchaseRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseRequest> result = purchaseRequestService.partialUpdate(purchaseRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchaseRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-requests} : get all the purchaseRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseRequests in body.
     */
    @GetMapping("/purchase-requests")
    public ResponseEntity<List<PurchaseRequest>> getAllPurchaseRequests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PurchaseRequests");
        Page<PurchaseRequest> page = purchaseRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchase-requests/:id} : get the "id" purchaseRequest.
     *
     * @param id the id of the purchaseRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-requests/{id}")
    public ResponseEntity<PurchaseRequest> getPurchaseRequest(@PathVariable Long id) {
        log.debug("REST request to get PurchaseRequest : {}", id);
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseRequest);
    }

    /**
     * {@code DELETE  /purchase-requests/:id} : delete the "id" purchaseRequest.
     *
     * @param id the id of the purchaseRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-requests/{id}")
    public ResponseEntity<Void> deletePurchaseRequest(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseRequest : {}", id);
        purchaseRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
