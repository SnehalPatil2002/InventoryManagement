package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PurchaseRequest;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.repository.PurchaseRequestRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PurchaseRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseRequestResourceIT {

    private static final Long DEFAULT_QTY_REQUIRED = 1L;
    private static final Long UPDATED_QTY_REQUIRED = 2L;

    private static final Instant DEFAULT_REQUEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPECTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_STATUS = Status.REQUESTED;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String ENTITY_API_URL = "/api/purchase-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseRequestMockMvc;

    private PurchaseRequest purchaseRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseRequest createEntity(EntityManager em) {
        PurchaseRequest purchaseRequest = new PurchaseRequest()
            .qtyRequired(DEFAULT_QTY_REQUIRED)
            .requestDate(DEFAULT_REQUEST_DATE)
            .expectedDate(DEFAULT_EXPECTED_DATE)
            .status(DEFAULT_STATUS);
        return purchaseRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseRequest createUpdatedEntity(EntityManager em) {
        PurchaseRequest purchaseRequest = new PurchaseRequest()
            .qtyRequired(UPDATED_QTY_REQUIRED)
            .requestDate(UPDATED_REQUEST_DATE)
            .expectedDate(UPDATED_EXPECTED_DATE)
            .status(UPDATED_STATUS);
        return purchaseRequest;
    }

    @BeforeEach
    public void initTest() {
        purchaseRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseRequest() throws Exception {
        int databaseSizeBeforeCreate = purchaseRequestRepository.findAll().size();
        // Create the PurchaseRequest
        restPurchaseRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseRequest testPurchaseRequest = purchaseRequestList.get(purchaseRequestList.size() - 1);
        assertThat(testPurchaseRequest.getQtyRequired()).isEqualTo(DEFAULT_QTY_REQUIRED);
        assertThat(testPurchaseRequest.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testPurchaseRequest.getExpectedDate()).isEqualTo(DEFAULT_EXPECTED_DATE);
        assertThat(testPurchaseRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createPurchaseRequestWithExistingId() throws Exception {
        // Create the PurchaseRequest with an existing ID
        purchaseRequest.setId(1L);

        int databaseSizeBeforeCreate = purchaseRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchaseRequests() throws Exception {
        // Initialize the database
        purchaseRequestRepository.saveAndFlush(purchaseRequest);

        // Get all the purchaseRequestList
        restPurchaseRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtyRequired").value(hasItem(DEFAULT_QTY_REQUIRED.intValue())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedDate").value(hasItem(DEFAULT_EXPECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPurchaseRequest() throws Exception {
        // Initialize the database
        purchaseRequestRepository.saveAndFlush(purchaseRequest);

        // Get the purchaseRequest
        restPurchaseRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseRequest.getId().intValue()))
            .andExpect(jsonPath("$.qtyRequired").value(DEFAULT_QTY_REQUIRED.intValue()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.expectedDate").value(DEFAULT_EXPECTED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseRequest() throws Exception {
        // Get the purchaseRequest
        restPurchaseRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchaseRequest() throws Exception {
        // Initialize the database
        purchaseRequestRepository.saveAndFlush(purchaseRequest);

        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();

        // Update the purchaseRequest
        PurchaseRequest updatedPurchaseRequest = purchaseRequestRepository.findById(purchaseRequest.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseRequest are not directly saved in db
        em.detach(updatedPurchaseRequest);
        updatedPurchaseRequest
            .qtyRequired(UPDATED_QTY_REQUIRED)
            .requestDate(UPDATED_REQUEST_DATE)
            .expectedDate(UPDATED_EXPECTED_DATE)
            .status(UPDATED_STATUS);

        restPurchaseRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseRequest))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
        PurchaseRequest testPurchaseRequest = purchaseRequestList.get(purchaseRequestList.size() - 1);
        assertThat(testPurchaseRequest.getQtyRequired()).isEqualTo(UPDATED_QTY_REQUIRED);
        assertThat(testPurchaseRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testPurchaseRequest.getExpectedDate()).isEqualTo(UPDATED_EXPECTED_DATE);
        assertThat(testPurchaseRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseRequest() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();
        purchaseRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseRequest() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();
        purchaseRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseRequest() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();
        purchaseRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseRequestWithPatch() throws Exception {
        // Initialize the database
        purchaseRequestRepository.saveAndFlush(purchaseRequest);

        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();

        // Update the purchaseRequest using partial update
        PurchaseRequest partialUpdatedPurchaseRequest = new PurchaseRequest();
        partialUpdatedPurchaseRequest.setId(purchaseRequest.getId());

        partialUpdatedPurchaseRequest.requestDate(UPDATED_REQUEST_DATE).expectedDate(UPDATED_EXPECTED_DATE).status(UPDATED_STATUS);

        restPurchaseRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseRequest))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
        PurchaseRequest testPurchaseRequest = purchaseRequestList.get(purchaseRequestList.size() - 1);
        assertThat(testPurchaseRequest.getQtyRequired()).isEqualTo(DEFAULT_QTY_REQUIRED);
        assertThat(testPurchaseRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testPurchaseRequest.getExpectedDate()).isEqualTo(UPDATED_EXPECTED_DATE);
        assertThat(testPurchaseRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseRequestWithPatch() throws Exception {
        // Initialize the database
        purchaseRequestRepository.saveAndFlush(purchaseRequest);

        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();

        // Update the purchaseRequest using partial update
        PurchaseRequest partialUpdatedPurchaseRequest = new PurchaseRequest();
        partialUpdatedPurchaseRequest.setId(purchaseRequest.getId());

        partialUpdatedPurchaseRequest
            .qtyRequired(UPDATED_QTY_REQUIRED)
            .requestDate(UPDATED_REQUEST_DATE)
            .expectedDate(UPDATED_EXPECTED_DATE)
            .status(UPDATED_STATUS);

        restPurchaseRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseRequest))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
        PurchaseRequest testPurchaseRequest = purchaseRequestList.get(purchaseRequestList.size() - 1);
        assertThat(testPurchaseRequest.getQtyRequired()).isEqualTo(UPDATED_QTY_REQUIRED);
        assertThat(testPurchaseRequest.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testPurchaseRequest.getExpectedDate()).isEqualTo(UPDATED_EXPECTED_DATE);
        assertThat(testPurchaseRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseRequest() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();
        purchaseRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseRequest() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();
        purchaseRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseRequest() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRequestRepository.findAll().size();
        purchaseRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseRequest in the database
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseRequest() throws Exception {
        // Initialize the database
        purchaseRequestRepository.saveAndFlush(purchaseRequest);

        int databaseSizeBeforeDelete = purchaseRequestRepository.findAll().size();

        // Delete the purchaseRequest
        restPurchaseRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseRequest> purchaseRequestList = purchaseRequestRepository.findAll();
        assertThat(purchaseRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
