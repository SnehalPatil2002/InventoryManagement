package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Clients;
import com.mycompany.myapp.repository.ClientsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Clients}.
 */
@Service
@Transactional
public class ClientsService {

    private final Logger log = LoggerFactory.getLogger(ClientsService.class);

    private final ClientsRepository clientsRepository;

    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    /**
     * Save a clients.
     *
     * @param clients the entity to save.
     * @return the persisted entity.
     */
    public Clients save(Clients clients) {
        log.debug("Request to save Clients : {}", clients);
        return clientsRepository.save(clients);
    }

    /**
     * Update a clients.
     *
     * @param clients the entity to save.
     * @return the persisted entity.
     */
    public Clients update(Clients clients) {
        log.debug("Request to update Clients : {}", clients);
        return clientsRepository.save(clients);
    }

    /**
     * Partially update a clients.
     *
     * @param clients the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Clients> partialUpdate(Clients clients) {
        log.debug("Request to partially update Clients : {}", clients);

        return clientsRepository
            .findById(clients.getId())
            .map(existingClients -> {
                if (clients.getSname() != null) {
                    existingClients.setSname(clients.getSname());
                }
                if (clients.getSemail() != null) {
                    existingClients.setSemail(clients.getSemail());
                }
                if (clients.getMobileNo() != null) {
                    existingClients.setMobileNo(clients.getMobileNo());
                }
                if (clients.getCompanyName() != null) {
                    existingClients.setCompanyName(clients.getCompanyName());
                }
                if (clients.getCompanyContactNo() != null) {
                    existingClients.setCompanyContactNo(clients.getCompanyContactNo());
                }
                if (clients.getAddress() != null) {
                    existingClients.setAddress(clients.getAddress());
                }
                if (clients.getPinCode() != null) {
                    existingClients.setPinCode(clients.getPinCode());
                }
                if (clients.getCity() != null) {
                    existingClients.setCity(clients.getCity());
                }
                if (clients.getClientType() != null) {
                    existingClients.setClientType(clients.getClientType());
                }

                return existingClients;
            })
            .map(clientsRepository::save);
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Clients> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientsRepository.findAll(pageable);
    }

    /**
     * Get one clients by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Clients> findOne(Long id) {
        log.debug("Request to get Clients : {}", id);
        return clientsRepository.findById(id);
    }

    /**
     * Delete the clients by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Clients : {}", id);
        clientsRepository.deleteById(id);
    }
}
