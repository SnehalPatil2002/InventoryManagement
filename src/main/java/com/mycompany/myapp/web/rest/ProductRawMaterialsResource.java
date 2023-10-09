package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProductRawMaterials;
import com.mycompany.myapp.repository.ProductRawMaterialsRepository;
import com.mycompany.myapp.service.ProductRawMaterialsService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProductRawMaterials}.
 */
@RestController
@RequestMapping("/api")
public class ProductRawMaterialsResource {

    private final Logger log = LoggerFactory.getLogger(ProductRawMaterialsResource.class);

    private static final String ENTITY_NAME = "productRawMaterials";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductRawMaterialsService productRawMaterialsService;

    private final ProductRawMaterialsRepository productRawMaterialsRepository;

    public ProductRawMaterialsResource(
        ProductRawMaterialsService productRawMaterialsService,
        ProductRawMaterialsRepository productRawMaterialsRepository
    ) {
        this.productRawMaterialsService = productRawMaterialsService;
        this.productRawMaterialsRepository = productRawMaterialsRepository;
    }

    /**
     * {@code POST  /product-raw-materials} : Create a new productRawMaterials.
     *
     * @param productRawMaterials the productRawMaterials to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productRawMaterials, or with status {@code 400 (Bad Request)} if the productRawMaterials has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-raw-materials")
    public ResponseEntity<ProductRawMaterials> createProductRawMaterials(@RequestBody ProductRawMaterials productRawMaterials)
        throws URISyntaxException {
        log.debug("REST request to save ProductRawMaterials : {}", productRawMaterials);
        if (productRawMaterials.getId() != null) {
            throw new BadRequestAlertException("A new productRawMaterials cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductRawMaterials result = productRawMaterialsService.save(productRawMaterials);
        return ResponseEntity
            .created(new URI("/api/product-raw-materials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-raw-materials/:id} : Updates an existing productRawMaterials.
     *
     * @param id the id of the productRawMaterials to save.
     * @param productRawMaterials the productRawMaterials to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productRawMaterials,
     * or with status {@code 400 (Bad Request)} if the productRawMaterials is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productRawMaterials couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-raw-materials/{id}")
    public ResponseEntity<ProductRawMaterials> updateProductRawMaterials(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductRawMaterials productRawMaterials
    ) throws URISyntaxException {
        log.debug("REST request to update ProductRawMaterials : {}, {}", id, productRawMaterials);
        if (productRawMaterials.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productRawMaterials.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRawMaterialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductRawMaterials result = productRawMaterialsService.update(productRawMaterials);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productRawMaterials.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-raw-materials/:id} : Partial updates given fields of an existing productRawMaterials, field will ignore if it is null
     *
     * @param id the id of the productRawMaterials to save.
     * @param productRawMaterials the productRawMaterials to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productRawMaterials,
     * or with status {@code 400 (Bad Request)} if the productRawMaterials is not valid,
     * or with status {@code 404 (Not Found)} if the productRawMaterials is not found,
     * or with status {@code 500 (Internal Server Error)} if the productRawMaterials couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-raw-materials/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductRawMaterials> partialUpdateProductRawMaterials(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductRawMaterials productRawMaterials
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductRawMaterials partially : {}, {}", id, productRawMaterials);
        if (productRawMaterials.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productRawMaterials.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRawMaterialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductRawMaterials> result = productRawMaterialsService.partialUpdate(productRawMaterials);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productRawMaterials.getId().toString())
        );
    }

    /**
     * {@code GET  /product-raw-materials} : get all the productRawMaterials.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productRawMaterials in body.
     */
    @GetMapping("/product-raw-materials")
    public ResponseEntity<List<ProductRawMaterials>> getAllProductRawMaterials(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProductRawMaterials");
        Page<ProductRawMaterials> page = productRawMaterialsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-raw-materials/:id} : get the "id" productRawMaterials.
     *
     * @param id the id of the productRawMaterials to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productRawMaterials, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-raw-materials/{id}")
    public ResponseEntity<ProductRawMaterials> getProductRawMaterials(@PathVariable Long id) {
        log.debug("REST request to get ProductRawMaterials : {}", id);
        Optional<ProductRawMaterials> productRawMaterials = productRawMaterialsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productRawMaterials);
    }

    /**
     * {@code DELETE  /product-raw-materials/:id} : delete the "id" productRawMaterials.
     *
     * @param id the id of the productRawMaterials to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-raw-materials/{id}")
    public ResponseEntity<Void> deleteProductRawMaterials(@PathVariable Long id) {
        log.debug("REST request to delete ProductRawMaterials : {}", id);
        productRawMaterialsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
