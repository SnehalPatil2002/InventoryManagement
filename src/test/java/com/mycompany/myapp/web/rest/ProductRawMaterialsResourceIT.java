package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductRawMaterials;
import com.mycompany.myapp.repository.ProductRawMaterialsRepository;
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
 * Integration tests for the {@link ProductRawMaterialsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductRawMaterialsResourceIT {

    private static final Long DEFAULT_QUANTITY_REQUIRED = 1L;
    private static final Long UPDATED_QUANTITY_REQUIRED = 2L;

    private static final String ENTITY_API_URL = "/api/product-raw-materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRawMaterialsRepository productRawMaterialsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductRawMaterialsMockMvc;

    private ProductRawMaterials productRawMaterials;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRawMaterials createEntity(EntityManager em) {
        ProductRawMaterials productRawMaterials = new ProductRawMaterials().quantityRequired(DEFAULT_QUANTITY_REQUIRED);
        return productRawMaterials;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRawMaterials createUpdatedEntity(EntityManager em) {
        ProductRawMaterials productRawMaterials = new ProductRawMaterials().quantityRequired(UPDATED_QUANTITY_REQUIRED);
        return productRawMaterials;
    }

    @BeforeEach
    public void initTest() {
        productRawMaterials = createEntity(em);
    }

    @Test
    @Transactional
    void createProductRawMaterials() throws Exception {
        int databaseSizeBeforeCreate = productRawMaterialsRepository.findAll().size();
        // Create the ProductRawMaterials
        restProductRawMaterialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isCreated());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeCreate + 1);
        ProductRawMaterials testProductRawMaterials = productRawMaterialsList.get(productRawMaterialsList.size() - 1);
        assertThat(testProductRawMaterials.getQuantityRequired()).isEqualTo(DEFAULT_QUANTITY_REQUIRED);
    }

    @Test
    @Transactional
    void createProductRawMaterialsWithExistingId() throws Exception {
        // Create the ProductRawMaterials with an existing ID
        productRawMaterials.setId(1L);

        int databaseSizeBeforeCreate = productRawMaterialsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductRawMaterialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductRawMaterials() throws Exception {
        // Initialize the database
        productRawMaterialsRepository.saveAndFlush(productRawMaterials);

        // Get all the productRawMaterialsList
        restProductRawMaterialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productRawMaterials.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityRequired").value(hasItem(DEFAULT_QUANTITY_REQUIRED.intValue())));
    }

    @Test
    @Transactional
    void getProductRawMaterials() throws Exception {
        // Initialize the database
        productRawMaterialsRepository.saveAndFlush(productRawMaterials);

        // Get the productRawMaterials
        restProductRawMaterialsMockMvc
            .perform(get(ENTITY_API_URL_ID, productRawMaterials.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productRawMaterials.getId().intValue()))
            .andExpect(jsonPath("$.quantityRequired").value(DEFAULT_QUANTITY_REQUIRED.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductRawMaterials() throws Exception {
        // Get the productRawMaterials
        restProductRawMaterialsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductRawMaterials() throws Exception {
        // Initialize the database
        productRawMaterialsRepository.saveAndFlush(productRawMaterials);

        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();

        // Update the productRawMaterials
        ProductRawMaterials updatedProductRawMaterials = productRawMaterialsRepository.findById(productRawMaterials.getId()).get();
        // Disconnect from session so that the updates on updatedProductRawMaterials are not directly saved in db
        em.detach(updatedProductRawMaterials);
        updatedProductRawMaterials.quantityRequired(UPDATED_QUANTITY_REQUIRED);

        restProductRawMaterialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductRawMaterials.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductRawMaterials))
            )
            .andExpect(status().isOk());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
        ProductRawMaterials testProductRawMaterials = productRawMaterialsList.get(productRawMaterialsList.size() - 1);
        assertThat(testProductRawMaterials.getQuantityRequired()).isEqualTo(UPDATED_QUANTITY_REQUIRED);
    }

    @Test
    @Transactional
    void putNonExistingProductRawMaterials() throws Exception {
        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();
        productRawMaterials.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductRawMaterialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productRawMaterials.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductRawMaterials() throws Exception {
        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();
        productRawMaterials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRawMaterialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductRawMaterials() throws Exception {
        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();
        productRawMaterials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRawMaterialsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductRawMaterialsWithPatch() throws Exception {
        // Initialize the database
        productRawMaterialsRepository.saveAndFlush(productRawMaterials);

        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();

        // Update the productRawMaterials using partial update
        ProductRawMaterials partialUpdatedProductRawMaterials = new ProductRawMaterials();
        partialUpdatedProductRawMaterials.setId(productRawMaterials.getId());

        restProductRawMaterialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRawMaterials.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductRawMaterials))
            )
            .andExpect(status().isOk());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
        ProductRawMaterials testProductRawMaterials = productRawMaterialsList.get(productRawMaterialsList.size() - 1);
        assertThat(testProductRawMaterials.getQuantityRequired()).isEqualTo(DEFAULT_QUANTITY_REQUIRED);
    }

    @Test
    @Transactional
    void fullUpdateProductRawMaterialsWithPatch() throws Exception {
        // Initialize the database
        productRawMaterialsRepository.saveAndFlush(productRawMaterials);

        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();

        // Update the productRawMaterials using partial update
        ProductRawMaterials partialUpdatedProductRawMaterials = new ProductRawMaterials();
        partialUpdatedProductRawMaterials.setId(productRawMaterials.getId());

        partialUpdatedProductRawMaterials.quantityRequired(UPDATED_QUANTITY_REQUIRED);

        restProductRawMaterialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRawMaterials.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductRawMaterials))
            )
            .andExpect(status().isOk());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
        ProductRawMaterials testProductRawMaterials = productRawMaterialsList.get(productRawMaterialsList.size() - 1);
        assertThat(testProductRawMaterials.getQuantityRequired()).isEqualTo(UPDATED_QUANTITY_REQUIRED);
    }

    @Test
    @Transactional
    void patchNonExistingProductRawMaterials() throws Exception {
        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();
        productRawMaterials.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductRawMaterialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productRawMaterials.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductRawMaterials() throws Exception {
        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();
        productRawMaterials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRawMaterialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductRawMaterials() throws Exception {
        int databaseSizeBeforeUpdate = productRawMaterialsRepository.findAll().size();
        productRawMaterials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRawMaterialsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productRawMaterials))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRawMaterials in the database
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductRawMaterials() throws Exception {
        // Initialize the database
        productRawMaterialsRepository.saveAndFlush(productRawMaterials);

        int databaseSizeBeforeDelete = productRawMaterialsRepository.findAll().size();

        // Delete the productRawMaterials
        restProductRawMaterialsMockMvc
            .perform(delete(ENTITY_API_URL_ID, productRawMaterials.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductRawMaterials> productRawMaterialsList = productRawMaterialsRepository.findAll();
        assertThat(productRawMaterialsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
