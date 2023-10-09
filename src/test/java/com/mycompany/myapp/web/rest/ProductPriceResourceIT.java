package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductPrice;
import com.mycompany.myapp.repository.ProductPriceRepository;
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
 * Integration tests for the {@link ProductPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductPriceResourceIT {

    private static final Double DEFAULT_RAW_MATERIAL_COST = 1D;
    private static final Double UPDATED_RAW_MATERIAL_COST = 2D;

    private static final Double DEFAULT_MANUFACTURING_COST = 1D;
    private static final Double UPDATED_MANUFACTURING_COST = 2D;

    private static final Double DEFAULT_LABOUR_COST = 1D;
    private static final Double UPDATED_LABOUR_COST = 2D;

    private static final String ENTITY_API_URL = "/api/product-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductPriceMockMvc;

    private ProductPrice productPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPrice createEntity(EntityManager em) {
        ProductPrice productPrice = new ProductPrice()
            .rawMaterialCost(DEFAULT_RAW_MATERIAL_COST)
            .manufacturingCost(DEFAULT_MANUFACTURING_COST)
            .labourCost(DEFAULT_LABOUR_COST);
        return productPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPrice createUpdatedEntity(EntityManager em) {
        ProductPrice productPrice = new ProductPrice()
            .rawMaterialCost(UPDATED_RAW_MATERIAL_COST)
            .manufacturingCost(UPDATED_MANUFACTURING_COST)
            .labourCost(UPDATED_LABOUR_COST);
        return productPrice;
    }

    @BeforeEach
    public void initTest() {
        productPrice = createEntity(em);
    }

    @Test
    @Transactional
    void createProductPrice() throws Exception {
        int databaseSizeBeforeCreate = productPriceRepository.findAll().size();
        // Create the ProductPrice
        restProductPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productPrice)))
            .andExpect(status().isCreated());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeCreate + 1);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getRawMaterialCost()).isEqualTo(DEFAULT_RAW_MATERIAL_COST);
        assertThat(testProductPrice.getManufacturingCost()).isEqualTo(DEFAULT_MANUFACTURING_COST);
        assertThat(testProductPrice.getLabourCost()).isEqualTo(DEFAULT_LABOUR_COST);
    }

    @Test
    @Transactional
    void createProductPriceWithExistingId() throws Exception {
        // Create the ProductPrice with an existing ID
        productPrice.setId(1L);

        int databaseSizeBeforeCreate = productPriceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductPriceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productPrice)))
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductPrices() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].rawMaterialCost").value(hasItem(DEFAULT_RAW_MATERIAL_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].manufacturingCost").value(hasItem(DEFAULT_MANUFACTURING_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].labourCost").value(hasItem(DEFAULT_LABOUR_COST.doubleValue())));
    }

    @Test
    @Transactional
    void getProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        // Get the productPrice
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, productPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productPrice.getId().intValue()))
            .andExpect(jsonPath("$.rawMaterialCost").value(DEFAULT_RAW_MATERIAL_COST.doubleValue()))
            .andExpect(jsonPath("$.manufacturingCost").value(DEFAULT_MANUFACTURING_COST.doubleValue()))
            .andExpect(jsonPath("$.labourCost").value(DEFAULT_LABOUR_COST.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductPrice() throws Exception {
        // Get the productPrice
        restProductPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();

        // Update the productPrice
        ProductPrice updatedProductPrice = productPriceRepository.findById(productPrice.getId()).get();
        // Disconnect from session so that the updates on updatedProductPrice are not directly saved in db
        em.detach(updatedProductPrice);
        updatedProductPrice
            .rawMaterialCost(UPDATED_RAW_MATERIAL_COST)
            .manufacturingCost(UPDATED_MANUFACTURING_COST)
            .labourCost(UPDATED_LABOUR_COST);

        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getRawMaterialCost()).isEqualTo(UPDATED_RAW_MATERIAL_COST);
        assertThat(testProductPrice.getManufacturingCost()).isEqualTo(UPDATED_MANUFACTURING_COST);
        assertThat(testProductPrice.getLabourCost()).isEqualTo(UPDATED_LABOUR_COST);
    }

    @Test
    @Transactional
    void putNonExistingProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPrice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productPrice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductPriceWithPatch() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();

        // Update the productPrice using partial update
        ProductPrice partialUpdatedProductPrice = new ProductPrice();
        partialUpdatedProductPrice.setId(productPrice.getId());

        partialUpdatedProductPrice.manufacturingCost(UPDATED_MANUFACTURING_COST).labourCost(UPDATED_LABOUR_COST);

        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getRawMaterialCost()).isEqualTo(DEFAULT_RAW_MATERIAL_COST);
        assertThat(testProductPrice.getManufacturingCost()).isEqualTo(UPDATED_MANUFACTURING_COST);
        assertThat(testProductPrice.getLabourCost()).isEqualTo(UPDATED_LABOUR_COST);
    }

    @Test
    @Transactional
    void fullUpdateProductPriceWithPatch() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();

        // Update the productPrice using partial update
        ProductPrice partialUpdatedProductPrice = new ProductPrice();
        partialUpdatedProductPrice.setId(productPrice.getId());

        partialUpdatedProductPrice
            .rawMaterialCost(UPDATED_RAW_MATERIAL_COST)
            .manufacturingCost(UPDATED_MANUFACTURING_COST)
            .labourCost(UPDATED_LABOUR_COST);

        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getRawMaterialCost()).isEqualTo(UPDATED_RAW_MATERIAL_COST);
        assertThat(testProductPrice.getManufacturingCost()).isEqualTo(UPDATED_MANUFACTURING_COST);
        assertThat(testProductPrice.getLabourCost()).isEqualTo(UPDATED_LABOUR_COST);
    }

    @Test
    @Transactional
    void patchNonExistingProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productPrice))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productPrice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeDelete = productPriceRepository.findAll().size();

        // Delete the productPrice
        restProductPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, productPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
