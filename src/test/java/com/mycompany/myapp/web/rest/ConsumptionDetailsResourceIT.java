package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ConsumptionDetails;
import com.mycompany.myapp.repository.ConsumptionDetailsRepository;
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
 * Integration tests for the {@link ConsumptionDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsumptionDetailsResourceIT {

    private static final Double DEFAULT_QUANTITY_CONSUMED = 1D;
    private static final Double UPDATED_QUANTITY_CONSUMED = 2D;

    private static final Double DEFAULT_SCRAP_GENERATED = 1D;
    private static final Double UPDATED_SCRAP_GENERATED = 2D;

    private static final String ENTITY_API_URL = "/api/consumption-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsumptionDetailsRepository consumptionDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumptionDetailsMockMvc;

    private ConsumptionDetails consumptionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumptionDetails createEntity(EntityManager em) {
        ConsumptionDetails consumptionDetails = new ConsumptionDetails()
            .quantityConsumed(DEFAULT_QUANTITY_CONSUMED)
            .scrapGenerated(DEFAULT_SCRAP_GENERATED);
        return consumptionDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumptionDetails createUpdatedEntity(EntityManager em) {
        ConsumptionDetails consumptionDetails = new ConsumptionDetails()
            .quantityConsumed(UPDATED_QUANTITY_CONSUMED)
            .scrapGenerated(UPDATED_SCRAP_GENERATED);
        return consumptionDetails;
    }

    @BeforeEach
    public void initTest() {
        consumptionDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createConsumptionDetails() throws Exception {
        int databaseSizeBeforeCreate = consumptionDetailsRepository.findAll().size();
        // Create the ConsumptionDetails
        restConsumptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isCreated());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ConsumptionDetails testConsumptionDetails = consumptionDetailsList.get(consumptionDetailsList.size() - 1);
        assertThat(testConsumptionDetails.getQuantityConsumed()).isEqualTo(DEFAULT_QUANTITY_CONSUMED);
        assertThat(testConsumptionDetails.getScrapGenerated()).isEqualTo(DEFAULT_SCRAP_GENERATED);
    }

    @Test
    @Transactional
    void createConsumptionDetailsWithExistingId() throws Exception {
        // Create the ConsumptionDetails with an existing ID
        consumptionDetails.setId(1L);

        int databaseSizeBeforeCreate = consumptionDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumptionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsumptionDetails() throws Exception {
        // Initialize the database
        consumptionDetailsRepository.saveAndFlush(consumptionDetails);

        // Get all the consumptionDetailsList
        restConsumptionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumptionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityConsumed").value(hasItem(DEFAULT_QUANTITY_CONSUMED.doubleValue())))
            .andExpect(jsonPath("$.[*].scrapGenerated").value(hasItem(DEFAULT_SCRAP_GENERATED.doubleValue())));
    }

    @Test
    @Transactional
    void getConsumptionDetails() throws Exception {
        // Initialize the database
        consumptionDetailsRepository.saveAndFlush(consumptionDetails);

        // Get the consumptionDetails
        restConsumptionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, consumptionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumptionDetails.getId().intValue()))
            .andExpect(jsonPath("$.quantityConsumed").value(DEFAULT_QUANTITY_CONSUMED.doubleValue()))
            .andExpect(jsonPath("$.scrapGenerated").value(DEFAULT_SCRAP_GENERATED.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingConsumptionDetails() throws Exception {
        // Get the consumptionDetails
        restConsumptionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsumptionDetails() throws Exception {
        // Initialize the database
        consumptionDetailsRepository.saveAndFlush(consumptionDetails);

        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();

        // Update the consumptionDetails
        ConsumptionDetails updatedConsumptionDetails = consumptionDetailsRepository.findById(consumptionDetails.getId()).get();
        // Disconnect from session so that the updates on updatedConsumptionDetails are not directly saved in db
        em.detach(updatedConsumptionDetails);
        updatedConsumptionDetails.quantityConsumed(UPDATED_QUANTITY_CONSUMED).scrapGenerated(UPDATED_SCRAP_GENERATED);

        restConsumptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsumptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConsumptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        ConsumptionDetails testConsumptionDetails = consumptionDetailsList.get(consumptionDetailsList.size() - 1);
        assertThat(testConsumptionDetails.getQuantityConsumed()).isEqualTo(UPDATED_QUANTITY_CONSUMED);
        assertThat(testConsumptionDetails.getScrapGenerated()).isEqualTo(UPDATED_SCRAP_GENERATED);
    }

    @Test
    @Transactional
    void putNonExistingConsumptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();
        consumptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumptionDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsumptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();
        consumptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsumptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();
        consumptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsumptionDetailsWithPatch() throws Exception {
        // Initialize the database
        consumptionDetailsRepository.saveAndFlush(consumptionDetails);

        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();

        // Update the consumptionDetails using partial update
        ConsumptionDetails partialUpdatedConsumptionDetails = new ConsumptionDetails();
        partialUpdatedConsumptionDetails.setId(consumptionDetails.getId());

        partialUpdatedConsumptionDetails.quantityConsumed(UPDATED_QUANTITY_CONSUMED).scrapGenerated(UPDATED_SCRAP_GENERATED);

        restConsumptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        ConsumptionDetails testConsumptionDetails = consumptionDetailsList.get(consumptionDetailsList.size() - 1);
        assertThat(testConsumptionDetails.getQuantityConsumed()).isEqualTo(UPDATED_QUANTITY_CONSUMED);
        assertThat(testConsumptionDetails.getScrapGenerated()).isEqualTo(UPDATED_SCRAP_GENERATED);
    }

    @Test
    @Transactional
    void fullUpdateConsumptionDetailsWithPatch() throws Exception {
        // Initialize the database
        consumptionDetailsRepository.saveAndFlush(consumptionDetails);

        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();

        // Update the consumptionDetails using partial update
        ConsumptionDetails partialUpdatedConsumptionDetails = new ConsumptionDetails();
        partialUpdatedConsumptionDetails.setId(consumptionDetails.getId());

        partialUpdatedConsumptionDetails.quantityConsumed(UPDATED_QUANTITY_CONSUMED).scrapGenerated(UPDATED_SCRAP_GENERATED);

        restConsumptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumptionDetails))
            )
            .andExpect(status().isOk());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
        ConsumptionDetails testConsumptionDetails = consumptionDetailsList.get(consumptionDetailsList.size() - 1);
        assertThat(testConsumptionDetails.getQuantityConsumed()).isEqualTo(UPDATED_QUANTITY_CONSUMED);
        assertThat(testConsumptionDetails.getScrapGenerated()).isEqualTo(UPDATED_SCRAP_GENERATED);
    }

    @Test
    @Transactional
    void patchNonExistingConsumptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();
        consumptionDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consumptionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsumptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();
        consumptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsumptionDetails() throws Exception {
        int databaseSizeBeforeUpdate = consumptionDetailsRepository.findAll().size();
        consumptionDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumptionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumptionDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumptionDetails in the database
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsumptionDetails() throws Exception {
        // Initialize the database
        consumptionDetailsRepository.saveAndFlush(consumptionDetails);

        int databaseSizeBeforeDelete = consumptionDetailsRepository.findAll().size();

        // Delete the consumptionDetails
        restConsumptionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, consumptionDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConsumptionDetails> consumptionDetailsList = consumptionDetailsRepository.findAll();
        assertThat(consumptionDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
