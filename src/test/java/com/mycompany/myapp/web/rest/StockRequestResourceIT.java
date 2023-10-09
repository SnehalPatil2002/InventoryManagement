package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StockRequest;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.repository.StockRequestRepository;
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
 * Integration tests for the {@link StockRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockRequestResourceIT {

    private static final Double DEFAULT_QTY_REQUIRED = 1D;
    private static final Double UPDATED_QTY_REQUIRED = 2D;

    private static final Instant DEFAULT_REQ_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQ_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_PROD = false;
    private static final Boolean UPDATED_IS_PROD = true;

    private static final Status DEFAULT_STATUS = Status.REQUESTED;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String ENTITY_API_URL = "/api/stock-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockRequestRepository stockRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockRequestMockMvc;

    private StockRequest stockRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockRequest createEntity(EntityManager em) {
        StockRequest stockRequest = new StockRequest()
            .qtyRequired(DEFAULT_QTY_REQUIRED)
            .reqDate(DEFAULT_REQ_DATE)
            .isProd(DEFAULT_IS_PROD)
            .status(DEFAULT_STATUS);
        return stockRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockRequest createUpdatedEntity(EntityManager em) {
        StockRequest stockRequest = new StockRequest()
            .qtyRequired(UPDATED_QTY_REQUIRED)
            .reqDate(UPDATED_REQ_DATE)
            .isProd(UPDATED_IS_PROD)
            .status(UPDATED_STATUS);
        return stockRequest;
    }

    @BeforeEach
    public void initTest() {
        stockRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createStockRequest() throws Exception {
        int databaseSizeBeforeCreate = stockRequestRepository.findAll().size();
        // Create the StockRequest
        restStockRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockRequest)))
            .andExpect(status().isCreated());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeCreate + 1);
        StockRequest testStockRequest = stockRequestList.get(stockRequestList.size() - 1);
        assertThat(testStockRequest.getQtyRequired()).isEqualTo(DEFAULT_QTY_REQUIRED);
        assertThat(testStockRequest.getReqDate()).isEqualTo(DEFAULT_REQ_DATE);
        assertThat(testStockRequest.getIsProd()).isEqualTo(DEFAULT_IS_PROD);
        assertThat(testStockRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createStockRequestWithExistingId() throws Exception {
        // Create the StockRequest with an existing ID
        stockRequest.setId(1L);

        int databaseSizeBeforeCreate = stockRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockRequest)))
            .andExpect(status().isBadRequest());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStockRequests() throws Exception {
        // Initialize the database
        stockRequestRepository.saveAndFlush(stockRequest);

        // Get all the stockRequestList
        restStockRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtyRequired").value(hasItem(DEFAULT_QTY_REQUIRED.doubleValue())))
            .andExpect(jsonPath("$.[*].reqDate").value(hasItem(DEFAULT_REQ_DATE.toString())))
            .andExpect(jsonPath("$.[*].isProd").value(hasItem(DEFAULT_IS_PROD.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getStockRequest() throws Exception {
        // Initialize the database
        stockRequestRepository.saveAndFlush(stockRequest);

        // Get the stockRequest
        restStockRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, stockRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockRequest.getId().intValue()))
            .andExpect(jsonPath("$.qtyRequired").value(DEFAULT_QTY_REQUIRED.doubleValue()))
            .andExpect(jsonPath("$.reqDate").value(DEFAULT_REQ_DATE.toString()))
            .andExpect(jsonPath("$.isProd").value(DEFAULT_IS_PROD.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStockRequest() throws Exception {
        // Get the stockRequest
        restStockRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockRequest() throws Exception {
        // Initialize the database
        stockRequestRepository.saveAndFlush(stockRequest);

        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();

        // Update the stockRequest
        StockRequest updatedStockRequest = stockRequestRepository.findById(stockRequest.getId()).get();
        // Disconnect from session so that the updates on updatedStockRequest are not directly saved in db
        em.detach(updatedStockRequest);
        updatedStockRequest.qtyRequired(UPDATED_QTY_REQUIRED).reqDate(UPDATED_REQ_DATE).isProd(UPDATED_IS_PROD).status(UPDATED_STATUS);

        restStockRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStockRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStockRequest))
            )
            .andExpect(status().isOk());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
        StockRequest testStockRequest = stockRequestList.get(stockRequestList.size() - 1);
        assertThat(testStockRequest.getQtyRequired()).isEqualTo(UPDATED_QTY_REQUIRED);
        assertThat(testStockRequest.getReqDate()).isEqualTo(UPDATED_REQ_DATE);
        assertThat(testStockRequest.getIsProd()).isEqualTo(UPDATED_IS_PROD);
        assertThat(testStockRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingStockRequest() throws Exception {
        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();
        stockRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockRequest() throws Exception {
        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();
        stockRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockRequest() throws Exception {
        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();
        stockRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockRequestWithPatch() throws Exception {
        // Initialize the database
        stockRequestRepository.saveAndFlush(stockRequest);

        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();

        // Update the stockRequest using partial update
        StockRequest partialUpdatedStockRequest = new StockRequest();
        partialUpdatedStockRequest.setId(stockRequest.getId());

        partialUpdatedStockRequest.isProd(UPDATED_IS_PROD);

        restStockRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockRequest))
            )
            .andExpect(status().isOk());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
        StockRequest testStockRequest = stockRequestList.get(stockRequestList.size() - 1);
        assertThat(testStockRequest.getQtyRequired()).isEqualTo(DEFAULT_QTY_REQUIRED);
        assertThat(testStockRequest.getReqDate()).isEqualTo(DEFAULT_REQ_DATE);
        assertThat(testStockRequest.getIsProd()).isEqualTo(UPDATED_IS_PROD);
        assertThat(testStockRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateStockRequestWithPatch() throws Exception {
        // Initialize the database
        stockRequestRepository.saveAndFlush(stockRequest);

        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();

        // Update the stockRequest using partial update
        StockRequest partialUpdatedStockRequest = new StockRequest();
        partialUpdatedStockRequest.setId(stockRequest.getId());

        partialUpdatedStockRequest
            .qtyRequired(UPDATED_QTY_REQUIRED)
            .reqDate(UPDATED_REQ_DATE)
            .isProd(UPDATED_IS_PROD)
            .status(UPDATED_STATUS);

        restStockRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockRequest))
            )
            .andExpect(status().isOk());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
        StockRequest testStockRequest = stockRequestList.get(stockRequestList.size() - 1);
        assertThat(testStockRequest.getQtyRequired()).isEqualTo(UPDATED_QTY_REQUIRED);
        assertThat(testStockRequest.getReqDate()).isEqualTo(UPDATED_REQ_DATE);
        assertThat(testStockRequest.getIsProd()).isEqualTo(UPDATED_IS_PROD);
        assertThat(testStockRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingStockRequest() throws Exception {
        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();
        stockRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockRequest() throws Exception {
        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();
        stockRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockRequest() throws Exception {
        int databaseSizeBeforeUpdate = stockRequestRepository.findAll().size();
        stockRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockRequestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stockRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockRequest in the database
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockRequest() throws Exception {
        // Initialize the database
        stockRequestRepository.saveAndFlush(stockRequest);

        int databaseSizeBeforeDelete = stockRequestRepository.findAll().size();

        // Delete the stockRequest
        restStockRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockRequest> stockRequestList = stockRequestRepository.findAll();
        assertThat(stockRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
