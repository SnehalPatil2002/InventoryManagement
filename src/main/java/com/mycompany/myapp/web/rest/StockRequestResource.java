package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StockRequest;
import com.mycompany.myapp.repository.StockRequestRepository;
import com.mycompany.myapp.service.StockRequestService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.StockRequest}.
 */
@RestController
@RequestMapping("/api")
public class StockRequestResource {

    private final Logger log = LoggerFactory.getLogger(StockRequestResource.class);

    private static final String ENTITY_NAME = "stockRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockRequestService stockRequestService;

    private final StockRequestRepository stockRequestRepository;

    public StockRequestResource(StockRequestService stockRequestService, StockRequestRepository stockRequestRepository) {
        this.stockRequestService = stockRequestService;
        this.stockRequestRepository = stockRequestRepository;
    }

    /**
     * {@code POST  /stock-requests} : Create a new stockRequest.
     *
     * @param stockRequest the stockRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockRequest, or with status {@code 400 (Bad Request)} if the stockRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stock-requests")
    public ResponseEntity<StockRequest> createStockRequest(@RequestBody StockRequest stockRequest) throws URISyntaxException {
        log.debug("REST request to save StockRequest : {}", stockRequest);
        if (stockRequest.getId() != null) {
            throw new BadRequestAlertException("A new stockRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockRequest result = stockRequestService.save(stockRequest);
        return ResponseEntity
            .created(new URI("/api/stock-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stock-requests/:id} : Updates an existing stockRequest.
     *
     * @param id the id of the stockRequest to save.
     * @param stockRequest the stockRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockRequest,
     * or with status {@code 400 (Bad Request)} if the stockRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stock-requests/{id}")
    public ResponseEntity<StockRequest> updateStockRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockRequest stockRequest
    ) throws URISyntaxException {
        log.debug("REST request to update StockRequest : {}, {}", id, stockRequest);
        if (stockRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StockRequest result = stockRequestService.update(stockRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stockRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stock-requests/:id} : Partial updates given fields of an existing stockRequest, field will ignore if it is null
     *
     * @param id the id of the stockRequest to save.
     * @param stockRequest the stockRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockRequest,
     * or with status {@code 400 (Bad Request)} if the stockRequest is not valid,
     * or with status {@code 404 (Not Found)} if the stockRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stock-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockRequest> partialUpdateStockRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockRequest stockRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update StockRequest partially : {}, {}", id, stockRequest);
        if (stockRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockRequest> result = stockRequestService.partialUpdate(stockRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stockRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-requests} : get all the stockRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockRequests in body.
     */
    @GetMapping("/stock-requests")
    public ResponseEntity<List<StockRequest>> getAllStockRequests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StockRequests");
        Page<StockRequest> page = stockRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-requests/:id} : get the "id" stockRequest.
     *
     * @param id the id of the stockRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stock-requests/{id}")
    public ResponseEntity<StockRequest> getStockRequest(@PathVariable Long id) {
        log.debug("REST request to get StockRequest : {}", id);
        Optional<StockRequest> stockRequest = stockRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockRequest);
    }

    /**
     * {@code DELETE  /stock-requests/:id} : delete the "id" stockRequest.
     *
     * @param id the id of the stockRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stock-requests/{id}")
    public ResponseEntity<Void> deleteStockRequest(@PathVariable Long id) {
        log.debug("REST request to delete StockRequest : {}", id);
        stockRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
