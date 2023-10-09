package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductPrice;
import com.mycompany.myapp.repository.ProductPriceRepository;
import com.mycompany.myapp.service.ProductPriceService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductPrice}.
 */
@RestController
@RequestMapping("/api")
public class ProductPriceResource {

    private final Logger log = LoggerFactory.getLogger(ProductPriceResource.class);

    private static final String ENTITY_NAME = "productPrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductPriceService productPriceService;

    private final ProductPriceRepository productPriceRepository;

    public ProductPriceResource(ProductPriceService productPriceService, ProductPriceRepository productPriceRepository) {
        this.productPriceService = productPriceService;
        this.productPriceRepository = productPriceRepository;
    }

    /**
     * {@code POST  /product-prices} : Create a new productPrice.
     *
     * @param productPrice the productPrice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productPrice, or with status {@code 400 (Bad Request)} if the productPrice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-prices")
    public ResponseEntity<ProductPrice> createProductPrice(@RequestBody ProductPrice productPrice) throws URISyntaxException {
        log.debug("REST request to save ProductPrice : {}", productPrice);
        if (productPrice.getId() != null) {
            throw new BadRequestAlertException("A new productPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductPrice result = productPriceService.save(productPrice);
        return ResponseEntity
            .created(new URI("/api/product-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-prices/:id} : Updates an existing productPrice.
     *
     * @param id the id of the productPrice to save.
     * @param productPrice the productPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPrice,
     * or with status {@code 400 (Bad Request)} if the productPrice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-prices/{id}")
    public ResponseEntity<ProductPrice> updateProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductPrice productPrice
    ) throws URISyntaxException {
        log.debug("REST request to update ProductPrice : {}, {}", id, productPrice);
        if (productPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductPrice result = productPriceService.update(productPrice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productPrice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-prices/:id} : Partial updates given fields of an existing productPrice, field will ignore if it is null
     *
     * @param id the id of the productPrice to save.
     * @param productPrice the productPrice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPrice,
     * or with status {@code 400 (Bad Request)} if the productPrice is not valid,
     * or with status {@code 404 (Not Found)} if the productPrice is not found,
     * or with status {@code 500 (Internal Server Error)} if the productPrice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-prices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductPrice> partialUpdateProductPrice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductPrice productPrice
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductPrice partially : {}, {}", id, productPrice);
        if (productPrice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productPrice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productPriceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductPrice> result = productPriceService.partialUpdate(productPrice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productPrice.getId().toString())
        );
    }

    /**
     * {@code GET  /product-prices} : get all the productPrices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productPrices in body.
     */
    @GetMapping("/product-prices")
    public ResponseEntity<List<ProductPrice>> getAllProductPrices(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProductPrices");
        Page<ProductPrice> page = productPriceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-prices/:id} : get the "id" productPrice.
     *
     * @param id the id of the productPrice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productPrice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-prices/{id}")
    public ResponseEntity<ProductPrice> getProductPrice(@PathVariable Long id) {
        log.debug("REST request to get ProductPrice : {}", id);
        Optional<ProductPrice> productPrice = productPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productPrice);
    }

    /**
     * {@code DELETE  /product-prices/:id} : delete the "id" productPrice.
     *
     * @param id the id of the productPrice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-prices/{id}")
    public ResponseEntity<Void> deleteProductPrice(@PathVariable Long id) {
        log.debug("REST request to delete ProductPrice : {}", id);
        productPriceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
