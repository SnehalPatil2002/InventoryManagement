package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RawMaterial;
import com.mycompany.myapp.repository.RawMaterialRepository;
import com.mycompany.myapp.service.RawMaterialService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RawMaterial}.
 */
@RestController
@RequestMapping("/api")
public class RawMaterialResource {

    private final Logger log = LoggerFactory.getLogger(RawMaterialResource.class);

    private static final String ENTITY_NAME = "rawMaterial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RawMaterialService rawMaterialService;

    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterialResource(RawMaterialService rawMaterialService, RawMaterialRepository rawMaterialRepository) {
        this.rawMaterialService = rawMaterialService;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    /**
     * {@code POST  /raw-materials} : Create a new rawMaterial.
     *
     * @param rawMaterial the rawMaterial to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rawMaterial, or with status {@code 400 (Bad Request)} if the rawMaterial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/raw-materials")
    public ResponseEntity<RawMaterial> createRawMaterial(@RequestBody RawMaterial rawMaterial) throws URISyntaxException {
        log.debug("REST request to save RawMaterial : {}", rawMaterial);
        if (rawMaterial.getId() != null) {
            throw new BadRequestAlertException("A new rawMaterial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RawMaterial result = rawMaterialService.save(rawMaterial);
        return ResponseEntity
            .created(new URI("/api/raw-materials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /raw-materials/:id} : Updates an existing rawMaterial.
     *
     * @param id the id of the rawMaterial to save.
     * @param rawMaterial the rawMaterial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rawMaterial,
     * or with status {@code 400 (Bad Request)} if the rawMaterial is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rawMaterial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/raw-materials/{id}")
    public ResponseEntity<RawMaterial> updateRawMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RawMaterial rawMaterial
    ) throws URISyntaxException {
        log.debug("REST request to update RawMaterial : {}, {}", id, rawMaterial);
        if (rawMaterial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rawMaterial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rawMaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RawMaterial result = rawMaterialService.update(rawMaterial);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rawMaterial.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /raw-materials/:id} : Partial updates given fields of an existing rawMaterial, field will ignore if it is null
     *
     * @param id the id of the rawMaterial to save.
     * @param rawMaterial the rawMaterial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rawMaterial,
     * or with status {@code 400 (Bad Request)} if the rawMaterial is not valid,
     * or with status {@code 404 (Not Found)} if the rawMaterial is not found,
     * or with status {@code 500 (Internal Server Error)} if the rawMaterial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/raw-materials/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RawMaterial> partialUpdateRawMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RawMaterial rawMaterial
    ) throws URISyntaxException {
        log.debug("REST request to partial update RawMaterial partially : {}, {}", id, rawMaterial);
        if (rawMaterial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rawMaterial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rawMaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RawMaterial> result = rawMaterialService.partialUpdate(rawMaterial);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rawMaterial.getId().toString())
        );
    }

    /**
     * {@code GET  /raw-materials} : get all the rawMaterials.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rawMaterials in body.
     */
    @GetMapping("/raw-materials")
    public ResponseEntity<List<RawMaterial>> getAllRawMaterials(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RawMaterials");
        Page<RawMaterial> page = rawMaterialService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /raw-materials/:id} : get the "id" rawMaterial.
     *
     * @param id the id of the rawMaterial to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rawMaterial, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/raw-materials/{id}")
    public ResponseEntity<RawMaterial> getRawMaterial(@PathVariable Long id) {
        log.debug("REST request to get RawMaterial : {}", id);
        Optional<RawMaterial> rawMaterial = rawMaterialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rawMaterial);
    }

    /**
     * {@code DELETE  /raw-materials/:id} : delete the "id" rawMaterial.
     *
     * @param id the id of the rawMaterial to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/raw-materials/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        log.debug("REST request to delete RawMaterial : {}", id);
        rawMaterialService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
