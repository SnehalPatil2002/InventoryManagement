package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Clients;
import com.mycompany.myapp.domain.enumeration.ClientType;
import com.mycompany.myapp.repository.ClientsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClientsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientsResourceIT {

    private static final String DEFAULT_SNAME = "AAAAAAAAAA";
    private static final String UPDATED_SNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SEMAIL = "AAAAAAAAAA";
    private static final String UPDATED_SEMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_MOBILE_NO = 1L;
    private static final Long UPDATED_MOBILE_NO = 2L;

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_CONTACT_NO = 1L;
    private static final Long UPDATED_COMPANY_CONTACT_NO = 2L;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PIN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final ClientType DEFAULT_CLIENT_TYPE = ClientType.SUPPLIER;
    private static final ClientType UPDATED_CLIENT_TYPE = ClientType.CONSUMER;

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientsMockMvc;

    private Clients clients;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clients createEntity(EntityManager em) {
        Clients clients = new Clients()
            .sname(DEFAULT_SNAME)
            .semail(DEFAULT_SEMAIL)
            .mobileNo(DEFAULT_MOBILE_NO)
            .companyName(DEFAULT_COMPANY_NAME)
            .companyContactNo(DEFAULT_COMPANY_CONTACT_NO)
            .address(DEFAULT_ADDRESS)
            .pinCode(DEFAULT_PIN_CODE)
            .city(DEFAULT_CITY)
            .clientType(DEFAULT_CLIENT_TYPE);
        return clients;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clients createUpdatedEntity(EntityManager em) {
        Clients clients = new Clients()
            .sname(UPDATED_SNAME)
            .semail(UPDATED_SEMAIL)
            .mobileNo(UPDATED_MOBILE_NO)
            .companyName(UPDATED_COMPANY_NAME)
            .companyContactNo(UPDATED_COMPANY_CONTACT_NO)
            .address(UPDATED_ADDRESS)
            .pinCode(UPDATED_PIN_CODE)
            .city(UPDATED_CITY)
            .clientType(UPDATED_CLIENT_TYPE);
        return clients;
    }

    @BeforeEach
    public void initTest() {
        clients = createEntity(em);
    }

    @Test
    @Transactional
    void createClients() throws Exception {
        int databaseSizeBeforeCreate = clientsRepository.findAll().size();
        // Create the Clients
        restClientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isCreated());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeCreate + 1);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getSname()).isEqualTo(DEFAULT_SNAME);
        assertThat(testClients.getSemail()).isEqualTo(DEFAULT_SEMAIL);
        assertThat(testClients.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testClients.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testClients.getCompanyContactNo()).isEqualTo(DEFAULT_COMPANY_CONTACT_NO);
        assertThat(testClients.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testClients.getPinCode()).isEqualTo(DEFAULT_PIN_CODE);
        assertThat(testClients.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testClients.getClientType()).isEqualTo(DEFAULT_CLIENT_TYPE);
    }

    @Test
    @Transactional
    void createClientsWithExistingId() throws Exception {
        // Create the Clients with an existing ID
        clients.setId(1L);

        int databaseSizeBeforeCreate = clientsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        // Get all the clientsList
        restClientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clients.getId().intValue())))
            .andExpect(jsonPath("$.[*].sname").value(hasItem(DEFAULT_SNAME)))
            .andExpect(jsonPath("$.[*].semail").value(hasItem(DEFAULT_SEMAIL)))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyContactNo").value(hasItem(DEFAULT_COMPANY_CONTACT_NO.intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].pinCode").value(hasItem(DEFAULT_PIN_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].clientType").value(hasItem(DEFAULT_CLIENT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        // Get the clients
        restClientsMockMvc
            .perform(get(ENTITY_API_URL_ID, clients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clients.getId().intValue()))
            .andExpect(jsonPath("$.sname").value(DEFAULT_SNAME))
            .andExpect(jsonPath("$.semail").value(DEFAULT_SEMAIL))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO.intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.companyContactNo").value(DEFAULT_COMPANY_CONTACT_NO.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.pinCode").value(DEFAULT_PIN_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.clientType").value(DEFAULT_CLIENT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClients() throws Exception {
        // Get the clients
        restClientsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();

        // Update the clients
        Clients updatedClients = clientsRepository.findById(clients.getId()).get();
        // Disconnect from session so that the updates on updatedClients are not directly saved in db
        em.detach(updatedClients);
        updatedClients
            .sname(UPDATED_SNAME)
            .semail(UPDATED_SEMAIL)
            .mobileNo(UPDATED_MOBILE_NO)
            .companyName(UPDATED_COMPANY_NAME)
            .companyContactNo(UPDATED_COMPANY_CONTACT_NO)
            .address(UPDATED_ADDRESS)
            .pinCode(UPDATED_PIN_CODE)
            .city(UPDATED_CITY)
            .clientType(UPDATED_CLIENT_TYPE);

        restClientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClients.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClients))
            )
            .andExpect(status().isOk());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getSname()).isEqualTo(UPDATED_SNAME);
        assertThat(testClients.getSemail()).isEqualTo(UPDATED_SEMAIL);
        assertThat(testClients.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testClients.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testClients.getCompanyContactNo()).isEqualTo(UPDATED_COMPANY_CONTACT_NO);
        assertThat(testClients.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClients.getPinCode()).isEqualTo(UPDATED_PIN_CODE);
        assertThat(testClients.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testClients.getClientType()).isEqualTo(UPDATED_CLIENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clients.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientsWithPatch() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();

        // Update the clients using partial update
        Clients partialUpdatedClients = new Clients();
        partialUpdatedClients.setId(clients.getId());

        partialUpdatedClients
            .sname(UPDATED_SNAME)
            .mobileNo(UPDATED_MOBILE_NO)
            .companyName(UPDATED_COMPANY_NAME)
            .companyContactNo(UPDATED_COMPANY_CONTACT_NO)
            .address(UPDATED_ADDRESS)
            .pinCode(UPDATED_PIN_CODE)
            .clientType(UPDATED_CLIENT_TYPE);

        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClients))
            )
            .andExpect(status().isOk());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getSname()).isEqualTo(UPDATED_SNAME);
        assertThat(testClients.getSemail()).isEqualTo(DEFAULT_SEMAIL);
        assertThat(testClients.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testClients.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testClients.getCompanyContactNo()).isEqualTo(UPDATED_COMPANY_CONTACT_NO);
        assertThat(testClients.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClients.getPinCode()).isEqualTo(UPDATED_PIN_CODE);
        assertThat(testClients.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testClients.getClientType()).isEqualTo(UPDATED_CLIENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateClientsWithPatch() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();

        // Update the clients using partial update
        Clients partialUpdatedClients = new Clients();
        partialUpdatedClients.setId(clients.getId());

        partialUpdatedClients
            .sname(UPDATED_SNAME)
            .semail(UPDATED_SEMAIL)
            .mobileNo(UPDATED_MOBILE_NO)
            .companyName(UPDATED_COMPANY_NAME)
            .companyContactNo(UPDATED_COMPANY_CONTACT_NO)
            .address(UPDATED_ADDRESS)
            .pinCode(UPDATED_PIN_CODE)
            .city(UPDATED_CITY)
            .clientType(UPDATED_CLIENT_TYPE);

        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClients))
            )
            .andExpect(status().isOk());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
        Clients testClients = clientsList.get(clientsList.size() - 1);
        assertThat(testClients.getSname()).isEqualTo(UPDATED_SNAME);
        assertThat(testClients.getSemail()).isEqualTo(UPDATED_SEMAIL);
        assertThat(testClients.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testClients.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testClients.getCompanyContactNo()).isEqualTo(UPDATED_COMPANY_CONTACT_NO);
        assertThat(testClients.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClients.getPinCode()).isEqualTo(UPDATED_PIN_CODE);
        assertThat(testClients.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testClients.getClientType()).isEqualTo(UPDATED_CLIENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClients() throws Exception {
        int databaseSizeBeforeUpdate = clientsRepository.findAll().size();
        clients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clients)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clients in the database
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClients() throws Exception {
        // Initialize the database
        clientsRepository.saveAndFlush(clients);

        int databaseSizeBeforeDelete = clientsRepository.findAll().size();

        // Delete the clients
        restClientsMockMvc
            .perform(delete(ENTITY_API_URL_ID, clients.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clients> clientsList = clientsRepository.findAll();
        assertThat(clientsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
