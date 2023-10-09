package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PurchaseQuotationDetails;
import com.mycompany.myapp.repository.PurchaseQuotationDetailsRepository;
import com.mycompany.myapp.service.PurchaseQuotationDetailsService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PurchaseQuotationDetails}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseQuotationDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseQuotationDetailsResource.class);

    private static final String ENTITY_NAME = "purchaseQuotationDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseQuotationDetailsService purchaseQuotationDetailsService;

    private final PurchaseQuotationDetailsRepository purchaseQuotationDetailsRepository;

    public PurchaseQuotationDetailsResource(
        PurchaseQuotationDetailsService purchaseQuotationDetailsService,
        PurchaseQuotationDetailsRepository purchaseQuotationDetailsRepository
    ) {
        this.purchaseQuotationDetailsService = purchaseQuotationDetailsService;
        this.purchaseQuotationDetailsRepository = purchaseQuotationDetailsRepository;
    }

    /**
     * {@code POST  /purchase-quotation-details} : Create a new purchaseQuotationDetails.
     *
     * @param purchaseQuotationDetails the purchaseQuotationDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseQuotationDetails, or with status {@code 400 (Bad Request)} if the purchaseQuotationDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-quotation-details")
    public ResponseEntity<PurchaseQuotationDetails> createPurchaseQuotationDetails(
        @RequestBody PurchaseQuotationDetails purchaseQuotationDetails
    ) throws URISyntaxException {
        log.debug("REST request to save PurchaseQuotationDetails : {}", purchaseQuotationDetails);
        if (purchaseQuotationDetails.getId() != null) {
            throw new BadRequestAlertException("A new purchaseQuotationDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseQuotationDetails result = purchaseQuotationDetailsService.save(purchaseQuotationDetails);
        return ResponseEntity
            .created(new URI("/api/purchase-quotation-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-quotation-details/:id} : Updates an existing purchaseQuotationDetails.
     *
     * @param id the id of the purchaseQuotationDetails to save.
     * @param purchaseQuotationDetails the purchaseQuotationDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseQuotationDetails,
     * or with status {@code 400 (Bad Request)} if the purchaseQuotationDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseQuotationDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-quotation-details/{id}")
    public ResponseEntity<PurchaseQuotationDetails> updatePurchaseQuotationDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseQuotationDetails purchaseQuotationDetails
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseQuotationDetails : {}, {}", id, purchaseQuotationDetails);
        if (purchaseQuotationDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseQuotationDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseQuotationDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseQuotationDetails result = purchaseQuotationDetailsService.update(purchaseQuotationDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchaseQuotationDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-quotation-details/:id} : Partial updates given fields of an existing purchaseQuotationDetails, field will ignore if it is null
     *
     * @param id the id of the purchaseQuotationDetails to save.
     * @param purchaseQuotationDetails the purchaseQuotationDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseQuotationDetails,
     * or with status {@code 400 (Bad Request)} if the purchaseQuotationDetails is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseQuotationDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseQuotationDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-quotation-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseQuotationDetails> partialUpdatePurchaseQuotationDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseQuotationDetails purchaseQuotationDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseQuotationDetails partially : {}, {}", id, purchaseQuotationDetails);
        if (purchaseQuotationDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseQuotationDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseQuotationDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseQuotationDetails> result = purchaseQuotationDetailsService.partialUpdate(purchaseQuotationDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, purchaseQuotationDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-quotation-details} : get all the purchaseQuotationDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseQuotationDetails in body.
     */
    @GetMapping("/purchase-quotation-details")
    public ResponseEntity<List<PurchaseQuotationDetails>> getAllPurchaseQuotationDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PurchaseQuotationDetails");
        Page<PurchaseQuotationDetails> page = purchaseQuotationDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchase-quotation-details/:id} : get the "id" purchaseQuotationDetails.
     *
     * @param id the id of the purchaseQuotationDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseQuotationDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-quotation-details/{id}")
    public ResponseEntity<PurchaseQuotationDetails> getPurchaseQuotationDetails(@PathVariable Long id) {
        log.debug("REST request to get PurchaseQuotationDetails : {}", id);
        Optional<PurchaseQuotationDetails> purchaseQuotationDetails = purchaseQuotationDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseQuotationDetails);
    }

    /**
     * {@code DELETE  /purchase-quotation-details/:id} : delete the "id" purchaseQuotationDetails.
     *
     * @param id the id of the purchaseQuotationDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-quotation-details/{id}")
    public ResponseEntity<Void> deletePurchaseQuotationDetails(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseQuotationDetails : {}", id);
        purchaseQuotationDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
