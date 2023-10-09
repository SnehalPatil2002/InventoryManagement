package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Clients;
import com.mycompany.myapp.repository.ClientsRepository;
import com.mycompany.myapp.service.ClientsService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Clients}.
 */
@RestController
@RequestMapping("/api")
public class ClientsResource {

    private final Logger log = LoggerFactory.getLogger(ClientsResource.class);

    private static final String ENTITY_NAME = "clients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientsService clientsService;

    private final ClientsRepository clientsRepository;

    public ClientsResource(ClientsService clientsService, ClientsRepository clientsRepository) {
        this.clientsService = clientsService;
        this.clientsRepository = clientsRepository;
    }

    /**
     * {@code POST  /clients} : Create a new clients.
     *
     * @param clients the clients to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clients, or with status {@code 400 (Bad Request)} if the clients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clients")
    public ResponseEntity<Clients> createClients(@RequestBody Clients clients) throws URISyntaxException {
        log.debug("REST request to save Clients : {}", clients);
        if (clients.getId() != null) {
            throw new BadRequestAlertException("A new clients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clients result = clientsService.save(clients);
        return ResponseEntity
            .created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clients/:id} : Updates an existing clients.
     *
     * @param id the id of the clients to save.
     * @param clients the clients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clients,
     * or with status {@code 400 (Bad Request)} if the clients is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clients/{id}")
    public ResponseEntity<Clients> updateClients(@PathVariable(value = "id", required = false) final Long id, @RequestBody Clients clients)
        throws URISyntaxException {
        log.debug("REST request to update Clients : {}, {}", id, clients);
        if (clients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clients result = clientsService.update(clients);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clients.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clients/:id} : Partial updates given fields of an existing clients, field will ignore if it is null
     *
     * @param id the id of the clients to save.
     * @param clients the clients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clients,
     * or with status {@code 400 (Bad Request)} if the clients is not valid,
     * or with status {@code 404 (Not Found)} if the clients is not found,
     * or with status {@code 500 (Internal Server Error)} if the clients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Clients> partialUpdateClients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Clients clients
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clients partially : {}, {}", id, clients);
        if (clients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clients> result = clientsService.partialUpdate(clients);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clients.getId().toString())
        );
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("/clients")
    public ResponseEntity<List<Clients>> getAllClients(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Clients");
        Page<Clients> page = clientsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clients/:id} : get the "id" clients.
     *
     * @param id the id of the clients to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clients, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public ResponseEntity<Clients> getClients(@PathVariable Long id) {
        log.debug("REST request to get Clients : {}", id);
        Optional<Clients> clients = clientsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clients);
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" clients.
     *
     * @param id the id of the clients to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> deleteClients(@PathVariable Long id) {
        log.debug("REST request to delete Clients : {}", id);
        clientsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
