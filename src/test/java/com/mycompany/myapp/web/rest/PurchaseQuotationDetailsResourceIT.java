package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PurchaseQuotationDetails;
import com.mycompany.myapp.repository.PurchaseQuotationDetailsRepository;
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
 * Integration tests for the {@link PurchaseQuotationDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseQuotationDetailsResourceIT {

    private static final Long DEFAULT_QTY_ORDERED = 1L;
    private static final Long UPDATED_QTY_ORDERED = 2L;

    private static final Integer DEFAULT_GST_TAX_PERCENTAGE = 1;
    private static final Integer UPDATED_GST_TAX_PERCENTAGE = 2;

    private static final Double DEFAULT_PRICE_PER_UNIT = 1D;
    private static final Double UPDATED_PRICE_PER_UNIT = 2D;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/purchase-quotation-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseQuotationDetailsRepository purchaseQuotationDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseQuotationDetailsMockMvc;

    private PurchaseQuotationDetails purchaseQuotationDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseQuotationDetails createEntity(EntityManager em) {
        PurchaseQuotationDetails purchaseQuotationDetails = new PurchaseQuotationDetails()
            .qtyOrdered(DEFAULT_QTY_ORDERED)
            .gstTaxPercentage(DEFAULT_GST_TAX_PERCENTAGE)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .discount(DEFAULT_DISCOUNT);
        return purchaseQuotationDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseQuotationDetails createUpdatedEntity(EntityManager em) {
        PurchaseQuotationDetails purchaseQuotationDetails = new PurchaseQuotationDetails()
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT);
        return purchaseQuotationDetails;
    }

    @BeforeEach
    public void initTest() {
        purchaseQuotationDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseQuotationDetails() throws Exception {
        int databaseSizeBeforeCreate = purchaseQuotationDetailsRepository.findAll().size();
        // Create the PurchaseQuotationDetails
        restPurchaseQuotationDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseQuotationDetails testPurchaseQuotationDetails = purchaseQuotationDetailsList.get(purchaseQuotationDetailsList.size() - 1);
        assertThat(testPurchaseQuotationDetails.getQtyOrdered()).isEqualTo(DEFAULT_QTY_ORDERED);
        assertThat(testPurchaseQuotationDetails.getGstTaxPercentage()).isEqualTo(DEFAULT_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseQuotationDetails.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testPurchaseQuotationDetails.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testPurchaseQuotationDetails.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
    }

    @Test
    @Transactional
    void createPurchaseQuotationDetailsWithExistingId() throws Exception {
        // Create the PurchaseQuotationDetails with an existing ID
        purchaseQuotationDetails.setId(1L);

        int databaseSizeBeforeCreate = purchaseQuotationDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseQuotationDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchaseQuotationDetails() throws Exception {
        // Initialize the database
        purchaseQuotationDetailsRepository.saveAndFlush(purchaseQuotationDetails);

        // Get all the purchaseQuotationDetailsList
        restPurchaseQuotationDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseQuotationDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].qtyOrdered").value(hasItem(DEFAULT_QTY_ORDERED.intValue())))
            .andExpect(jsonPath("$.[*].gstTaxPercentage").value(hasItem(DEFAULT_GST_TAX_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getPurchaseQuotationDetails() throws Exception {
        // Initialize the database
        purchaseQuotationDetailsRepository.saveAndFlush(purchaseQuotationDetails);

        // Get the purchaseQuotationDetails
        restPurchaseQuotationDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseQuotationDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseQuotationDetails.getId().intValue()))
            .andExpect(jsonPath("$.qtyOrdered").value(DEFAULT_QTY_ORDERED.intValue()))
            .andExpect(jsonPath("$.gstTaxPercentage").value(DEFAULT_GST_TAX_PERCENTAGE))
            .andExpect(jsonPath("$.pricePerUnit").value(DEFAULT_PRICE_PER_UNIT.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseQuotationDetails() throws Exception {
        // Get the purchaseQuotationDetails
        restPurchaseQuotationDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchaseQuotationDetails() throws Exception {
        // Initialize the database
        purchaseQuotationDetailsRepository.saveAndFlush(purchaseQuotationDetails);

        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();

        // Update the purchaseQuotationDetails
        PurchaseQuotationDetails updatedPurchaseQuotationDetails = purchaseQuotationDetailsRepository
            .findById(purchaseQuotationDetails.getId())
            .get();
        // Disconnect from session so that the updates on updatedPurchaseQuotationDetails are not directly saved in db
        em.detach(updatedPurchaseQuotationDetails);
        updatedPurchaseQuotationDetails
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT);

        restPurchaseQuotationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseQuotationDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseQuotationDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseQuotationDetails testPurchaseQuotationDetails = purchaseQuotationDetailsList.get(purchaseQuotationDetailsList.size() - 1);
        assertThat(testPurchaseQuotationDetails.getQtyOrdered()).isEqualTo(UPDATED_QTY_ORDERED);
        assertThat(testPurchaseQuotationDetails.getGstTaxPercentage()).isEqualTo(UPDATED_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseQuotationDetails.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testPurchaseQuotationDetails.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testPurchaseQuotationDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseQuotationDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();
        purchaseQuotationDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseQuotationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseQuotationDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseQuotationDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();
        purchaseQuotationDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseQuotationDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();
        purchaseQuotationDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseQuotationDetailsWithPatch() throws Exception {
        // Initialize the database
        purchaseQuotationDetailsRepository.saveAndFlush(purchaseQuotationDetails);

        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();

        // Update the purchaseQuotationDetails using partial update
        PurchaseQuotationDetails partialUpdatedPurchaseQuotationDetails = new PurchaseQuotationDetails();
        partialUpdatedPurchaseQuotationDetails.setId(purchaseQuotationDetails.getId());

        partialUpdatedPurchaseQuotationDetails
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT);

        restPurchaseQuotationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseQuotationDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseQuotationDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseQuotationDetails testPurchaseQuotationDetails = purchaseQuotationDetailsList.get(purchaseQuotationDetailsList.size() - 1);
        assertThat(testPurchaseQuotationDetails.getQtyOrdered()).isEqualTo(UPDATED_QTY_ORDERED);
        assertThat(testPurchaseQuotationDetails.getGstTaxPercentage()).isEqualTo(UPDATED_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseQuotationDetails.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testPurchaseQuotationDetails.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testPurchaseQuotationDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseQuotationDetailsWithPatch() throws Exception {
        // Initialize the database
        purchaseQuotationDetailsRepository.saveAndFlush(purchaseQuotationDetails);

        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();

        // Update the purchaseQuotationDetails using partial update
        PurchaseQuotationDetails partialUpdatedPurchaseQuotationDetails = new PurchaseQuotationDetails();
        partialUpdatedPurchaseQuotationDetails.setId(purchaseQuotationDetails.getId());

        partialUpdatedPurchaseQuotationDetails
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .gstTaxPercentage(UPDATED_GST_TAX_PERCENTAGE)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT);

        restPurchaseQuotationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseQuotationDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseQuotationDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseQuotationDetails testPurchaseQuotationDetails = purchaseQuotationDetailsList.get(purchaseQuotationDetailsList.size() - 1);
        assertThat(testPurchaseQuotationDetails.getQtyOrdered()).isEqualTo(UPDATED_QTY_ORDERED);
        assertThat(testPurchaseQuotationDetails.getGstTaxPercentage()).isEqualTo(UPDATED_GST_TAX_PERCENTAGE);
        assertThat(testPurchaseQuotationDetails.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testPurchaseQuotationDetails.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testPurchaseQuotationDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseQuotationDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();
        purchaseQuotationDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseQuotationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseQuotationDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseQuotationDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();
        purchaseQuotationDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseQuotationDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseQuotationDetailsRepository.findAll().size();
        purchaseQuotationDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseQuotationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseQuotationDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseQuotationDetails in the database
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseQuotationDetails() throws Exception {
        // Initialize the database
        purchaseQuotationDetailsRepository.saveAndFlush(purchaseQuotationDetails);

        int databaseSizeBeforeDelete = purchaseQuotationDetailsRepository.findAll().size();

        // Delete the purchaseQuotationDetails
        restPurchaseQuotationDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseQuotationDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseQuotationDetails> purchaseQuotationDetailsList = purchaseQuotationDetailsRepository.findAll();
        assertThat(purchaseQuotationDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
