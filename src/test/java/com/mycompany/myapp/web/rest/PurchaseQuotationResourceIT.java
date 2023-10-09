package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PurchaseQuotation;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.repository.PurchaseQuotationRepository;
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
 * Integration tests for the {@link PurchaseQuotationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseQuotationResourceIT {

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_PO_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_PO_AMOUNT = 2D;

    private static final Double DEFAULT_TOTAL_GST_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_GST_AMOUNT = 2D;

    private static final Instant DEFAULT_PO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPECTED_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Status DEFAULT_ORDER_STATUS = Status.REQUESTED;
    private static final Status UPDATED_ORDER_STATUS = Status.APPROVED;

    private static final String ENTITY_API_URL = "/api/purchase-quotations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseQuotationRepository purchaseQuotationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseQuotationMockMvc;

    private PurchaseQuotation purchaseQuotation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseQuotation createEntity(EntityManager em) {
        PurchaseQuotation purchaseQuotation = new PurchaseQuotation()
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .totalPOAmount(DEFAULT_TOTAL_PO_AMOUNT)
            .totalGSTAmount(DEFAULT_TOTAL_GST_AMOUNT)
            .poDate(DEFAULT_PO_DATE)
            .expectedDeliveryDate(DEFAULT_EXPECTED_DELIVERY_DATE)
            .orderStatus(DEFAULT_ORDER_STATUS);
        return purchaseQuotation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseQuotation createUpdatedEntity(EntityManager em) {
        PurchaseQuotation purchaseQuotation = new PurchaseQuotation()
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .totalPOAmount(UPDATED_TOTAL_PO_AMOUNT)
            .totalGSTAmount(UPDATED_TOTAL_GST_AMOUNT)
            .poDate(UPDATED_PO_DATE)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .orderStatus(UPDATED_ORDER_STATUS);
        return purchaseQuotation;
    }

    @BeforeEach
    public void initTest() {
        purchaseQuotation = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseQuotation() throws Exception {
        int databaseSizeBeforeCreate = purchaseQuotationRepository.findAll().size();
        // Create the PurchaseQuotation
        restPurchaseQuotationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseQuotation testPurchaseQuotation = purchaseQuotationList.get(purchaseQuotationList.size() - 1);
        assertThat(testPurchaseQuotation.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testPurchaseQuotation.getTotalPOAmount()).isEqualTo(DEFAULT_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseQuotation.getTotalGSTAmount()).isEqualTo(DEFAULT_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseQuotation.getPoDate()).isEqualTo(DEFAULT_PO_DATE);
        assertThat(testPurchaseQuotation.getExpectedDeliveryDate()).isEqualTo(DEFAULT_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseQuotation.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
    }

    @Test
    @Transactional
    void createPurchaseQuotationWithExistingId() throws Exception {
        // Create the PurchaseQuotation with an existing ID
        purchaseQuotation.setId(1L);

        int databaseSizeBeforeCreate = purchaseQuotationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseQuotationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchaseQuotations() throws Exception {
        // Initialize the database
        purchaseQuotationRepository.saveAndFlush(purchaseQuotation);

        // Get all the purchaseQuotationList
        restPurchaseQuotationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseQuotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].totalPOAmount").value(hasItem(DEFAULT_TOTAL_PO_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalGSTAmount").value(hasItem(DEFAULT_TOTAL_GST_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].poDate").value(hasItem(DEFAULT_PO_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedDeliveryDate").value(hasItem(DEFAULT_EXPECTED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPurchaseQuotation() throws Exception {
        // Initialize the database
        purchaseQuotationRepository.saveAndFlush(purchaseQuotation);

        // Get the purchaseQuotation
        restPurchaseQuotationMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseQuotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseQuotation.getId().intValue()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.totalPOAmount").value(DEFAULT_TOTAL_PO_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalGSTAmount").value(DEFAULT_TOTAL_GST_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.poDate").value(DEFAULT_PO_DATE.toString()))
            .andExpect(jsonPath("$.expectedDeliveryDate").value(DEFAULT_EXPECTED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseQuotation() throws Exception {
        // Get the purchaseQuotation
        restPurchaseQuotationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchaseQuotation() throws Exception {
        // Initialize the database
        purchaseQuotationRepository.saveAndFlush(purchaseQuotation);

        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();

        // Update the purchaseQuotation
        PurchaseQuotation updatedPurchaseQuotation = purchaseQuotationRepository.findById(purchaseQuotation.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseQuotation are not directly saved in db
        em.detach(updatedPurchaseQuotation);
        updatedPurchaseQuotation
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .totalPOAmount(UPDATED_TOTAL_PO_AMOUNT)
            .totalGSTAmount(UPDATED_TOTAL_GST_AMOUNT)
            .poDate(UPDATED_PO_DATE)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .orderStatus(UPDATED_ORDER_STATUS);

        restPurchaseQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseQuotation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseQuotation))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
        PurchaseQuotation testPurchaseQuotation = purchaseQuotationList.get(purchaseQuotationList.size() - 1);
        assertThat(testPurchaseQuotation.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testPurchaseQuotation.getTotalPOAmount()).isEqualTo(UPDATED_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseQuotation.getTotalGSTAmount()).isEqualTo(UPDATED_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseQuotation.getPoDate()).isEqualTo(UPDATED_PO_DATE);
        assertThat(testPurchaseQuotation.getExpectedDeliveryDate()).isEqualTo(UPDATED_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseQuotation.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseQuotation() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();
        purchaseQuotation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseQuotation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseQuotation() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();
        purchaseQuotation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseQuotation() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();
        purchaseQuotation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseQuotationWithPatch() throws Exception {
        // Initialize the database
        purchaseQuotationRepository.saveAndFlush(purchaseQuotation);

        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();

        // Update the purchaseQuotation using partial update
        PurchaseQuotation partialUpdatedPurchaseQuotation = new PurchaseQuotation();
        partialUpdatedPurchaseQuotation.setId(purchaseQuotation.getId());

        partialUpdatedPurchaseQuotation.poDate(UPDATED_PO_DATE).orderStatus(UPDATED_ORDER_STATUS);

        restPurchaseQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseQuotation))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
        PurchaseQuotation testPurchaseQuotation = purchaseQuotationList.get(purchaseQuotationList.size() - 1);
        assertThat(testPurchaseQuotation.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testPurchaseQuotation.getTotalPOAmount()).isEqualTo(DEFAULT_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseQuotation.getTotalGSTAmount()).isEqualTo(DEFAULT_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseQuotation.getPoDate()).isEqualTo(UPDATED_PO_DATE);
        assertThat(testPurchaseQuotation.getExpectedDeliveryDate()).isEqualTo(DEFAULT_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseQuotation.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseQuotationWithPatch() throws Exception {
        // Initialize the database
        purchaseQuotationRepository.saveAndFlush(purchaseQuotation);

        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();

        // Update the purchaseQuotation using partial update
        PurchaseQuotation partialUpdatedPurchaseQuotation = new PurchaseQuotation();
        partialUpdatedPurchaseQuotation.setId(purchaseQuotation.getId());

        partialUpdatedPurchaseQuotation
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .totalPOAmount(UPDATED_TOTAL_PO_AMOUNT)
            .totalGSTAmount(UPDATED_TOTAL_GST_AMOUNT)
            .poDate(UPDATED_PO_DATE)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .orderStatus(UPDATED_ORDER_STATUS);

        restPurchaseQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseQuotation))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
        PurchaseQuotation testPurchaseQuotation = purchaseQuotationList.get(purchaseQuotationList.size() - 1);
        assertThat(testPurchaseQuotation.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testPurchaseQuotation.getTotalPOAmount()).isEqualTo(UPDATED_TOTAL_PO_AMOUNT);
        assertThat(testPurchaseQuotation.getTotalGSTAmount()).isEqualTo(UPDATED_TOTAL_GST_AMOUNT);
        assertThat(testPurchaseQuotation.getPoDate()).isEqualTo(UPDATED_PO_DATE);
        assertThat(testPurchaseQuotation.getExpectedDeliveryDate()).isEqualTo(UPDATED_EXPECTED_DELIVERY_DATE);
        assertThat(testPurchaseQuotation.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseQuotation() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();
        purchaseQuotation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseQuotation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseQuotation() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();
        purchaseQuotation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseQuotation() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationRepository.findAll().size();
        purchaseQuotation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseQuotation in the database
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseQuotation() throws Exception {
        // Initialize the database
        purchaseQuotationRepository.saveAndFlush(purchaseQuotation);

        int databaseSizeBeforeDelete = purchaseQuotationRepository.findAll().size();

        // Delete the purchaseQuotation
        restPurchaseQuotationMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseQuotation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseQuotation> purchaseQuotationList = purchaseQuotationRepository.findAll();
        assertThat(purchaseQuotationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
