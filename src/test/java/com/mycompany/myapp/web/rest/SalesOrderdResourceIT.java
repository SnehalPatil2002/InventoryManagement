package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SalesOrderd;
import com.mycompany.myapp.domain.enumeration.DeliveryStatus;
import com.mycompany.myapp.repository.SalesOrderdRepository;
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
 * Integration tests for the {@link SalesOrderdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalesOrderdResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_QUANTITY_SOLD = 1L;
    private static final Long UPDATED_QUANTITY_SOLD = 2L;

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;

    private static final Double DEFAULT_GST_PERCENTAGE = 1D;
    private static final Double UPDATED_GST_PERCENTAGE = 2D;

    private static final Double DEFAULT_TOTAL_REVENUE = 1D;
    private static final Double UPDATED_TOTAL_REVENUE = 2D;

    private static final DeliveryStatus DEFAULT_STATUS = DeliveryStatus.ORDERED;
    private static final DeliveryStatus UPDATED_STATUS = DeliveryStatus.SHIPPED;

    private static final String ENTITY_API_URL = "/api/sales-orderds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalesOrderdRepository salesOrderdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalesOrderdMockMvc;

    private SalesOrderd salesOrderd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesOrderd createEntity(EntityManager em) {
        SalesOrderd salesOrderd = new SalesOrderd()
            .orderDate(DEFAULT_ORDER_DATE)
            .quantitySold(DEFAULT_QUANTITY_SOLD)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .gstPercentage(DEFAULT_GST_PERCENTAGE)
            .totalRevenue(DEFAULT_TOTAL_REVENUE)
            .status(DEFAULT_STATUS);
        return salesOrderd;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesOrderd createUpdatedEntity(EntityManager em) {
        SalesOrderd salesOrderd = new SalesOrderd()
            .orderDate(UPDATED_ORDER_DATE)
            .quantitySold(UPDATED_QUANTITY_SOLD)
            .unitPrice(UPDATED_UNIT_PRICE)
            .gstPercentage(UPDATED_GST_PERCENTAGE)
            .totalRevenue(UPDATED_TOTAL_REVENUE)
            .status(UPDATED_STATUS);
        return salesOrderd;
    }

    @BeforeEach
    public void initTest() {
        salesOrderd = createEntity(em);
    }

    @Test
    @Transactional
    void createSalesOrderd() throws Exception {
        int databaseSizeBeforeCreate = salesOrderdRepository.findAll().size();
        // Create the SalesOrderd
        restSalesOrderdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesOrderd)))
            .andExpect(status().isCreated());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeCreate + 1);
        SalesOrderd testSalesOrderd = salesOrderdList.get(salesOrderdList.size() - 1);
        assertThat(testSalesOrderd.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testSalesOrderd.getQuantitySold()).isEqualTo(DEFAULT_QUANTITY_SOLD);
        assertThat(testSalesOrderd.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testSalesOrderd.getGstPercentage()).isEqualTo(DEFAULT_GST_PERCENTAGE);
        assertThat(testSalesOrderd.getTotalRevenue()).isEqualTo(DEFAULT_TOTAL_REVENUE);
        assertThat(testSalesOrderd.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createSalesOrderdWithExistingId() throws Exception {
        // Create the SalesOrderd with an existing ID
        salesOrderd.setId(1L);

        int databaseSizeBeforeCreate = salesOrderdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesOrderdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesOrderd)))
            .andExpect(status().isBadRequest());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalesOrderds() throws Exception {
        // Initialize the database
        salesOrderdRepository.saveAndFlush(salesOrderd);

        // Get all the salesOrderdList
        restSalesOrderdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesOrderd.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantitySold").value(hasItem(DEFAULT_QUANTITY_SOLD.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].gstPercentage").value(hasItem(DEFAULT_GST_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalRevenue").value(hasItem(DEFAULT_TOTAL_REVENUE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSalesOrderd() throws Exception {
        // Initialize the database
        salesOrderdRepository.saveAndFlush(salesOrderd);

        // Get the salesOrderd
        restSalesOrderdMockMvc
            .perform(get(ENTITY_API_URL_ID, salesOrderd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salesOrderd.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.quantitySold").value(DEFAULT_QUANTITY_SOLD.intValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.gstPercentage").value(DEFAULT_GST_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.totalRevenue").value(DEFAULT_TOTAL_REVENUE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSalesOrderd() throws Exception {
        // Get the salesOrderd
        restSalesOrderdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalesOrderd() throws Exception {
        // Initialize the database
        salesOrderdRepository.saveAndFlush(salesOrderd);

        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();

        // Update the salesOrderd
        SalesOrderd updatedSalesOrderd = salesOrderdRepository.findById(salesOrderd.getId()).get();
        // Disconnect from session so that the updates on updatedSalesOrderd are not directly saved in db
        em.detach(updatedSalesOrderd);
        updatedSalesOrderd
            .orderDate(UPDATED_ORDER_DATE)
            .quantitySold(UPDATED_QUANTITY_SOLD)
            .unitPrice(UPDATED_UNIT_PRICE)
            .gstPercentage(UPDATED_GST_PERCENTAGE)
            .totalRevenue(UPDATED_TOTAL_REVENUE)
            .status(UPDATED_STATUS);

        restSalesOrderdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSalesOrderd.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSalesOrderd))
            )
            .andExpect(status().isOk());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
        SalesOrderd testSalesOrderd = salesOrderdList.get(salesOrderdList.size() - 1);
        assertThat(testSalesOrderd.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testSalesOrderd.getQuantitySold()).isEqualTo(UPDATED_QUANTITY_SOLD);
        assertThat(testSalesOrderd.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testSalesOrderd.getGstPercentage()).isEqualTo(UPDATED_GST_PERCENTAGE);
        assertThat(testSalesOrderd.getTotalRevenue()).isEqualTo(UPDATED_TOTAL_REVENUE);
        assertThat(testSalesOrderd.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSalesOrderd() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();
        salesOrderd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesOrderdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salesOrderd.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesOrderd))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalesOrderd() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();
        salesOrderd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesOrderdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesOrderd))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalesOrderd() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();
        salesOrderd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesOrderdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesOrderd)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalesOrderdWithPatch() throws Exception {
        // Initialize the database
        salesOrderdRepository.saveAndFlush(salesOrderd);

        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();

        // Update the salesOrderd using partial update
        SalesOrderd partialUpdatedSalesOrderd = new SalesOrderd();
        partialUpdatedSalesOrderd.setId(salesOrderd.getId());

        partialUpdatedSalesOrderd.unitPrice(UPDATED_UNIT_PRICE).totalRevenue(UPDATED_TOTAL_REVENUE);

        restSalesOrderdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesOrderd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalesOrderd))
            )
            .andExpect(status().isOk());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
        SalesOrderd testSalesOrderd = salesOrderdList.get(salesOrderdList.size() - 1);
        assertThat(testSalesOrderd.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testSalesOrderd.getQuantitySold()).isEqualTo(DEFAULT_QUANTITY_SOLD);
        assertThat(testSalesOrderd.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testSalesOrderd.getGstPercentage()).isEqualTo(DEFAULT_GST_PERCENTAGE);
        assertThat(testSalesOrderd.getTotalRevenue()).isEqualTo(UPDATED_TOTAL_REVENUE);
        assertThat(testSalesOrderd.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSalesOrderdWithPatch() throws Exception {
        // Initialize the database
        salesOrderdRepository.saveAndFlush(salesOrderd);

        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();

        // Update the salesOrderd using partial update
        SalesOrderd partialUpdatedSalesOrderd = new SalesOrderd();
        partialUpdatedSalesOrderd.setId(salesOrderd.getId());

        partialUpdatedSalesOrderd
            .orderDate(UPDATED_ORDER_DATE)
            .quantitySold(UPDATED_QUANTITY_SOLD)
            .unitPrice(UPDATED_UNIT_PRICE)
            .gstPercentage(UPDATED_GST_PERCENTAGE)
            .totalRevenue(UPDATED_TOTAL_REVENUE)
            .status(UPDATED_STATUS);

        restSalesOrderdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesOrderd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalesOrderd))
            )
            .andExpect(status().isOk());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
        SalesOrderd testSalesOrderd = salesOrderdList.get(salesOrderdList.size() - 1);
        assertThat(testSalesOrderd.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testSalesOrderd.getQuantitySold()).isEqualTo(UPDATED_QUANTITY_SOLD);
        assertThat(testSalesOrderd.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testSalesOrderd.getGstPercentage()).isEqualTo(UPDATED_GST_PERCENTAGE);
        assertThat(testSalesOrderd.getTotalRevenue()).isEqualTo(UPDATED_TOTAL_REVENUE);
        assertThat(testSalesOrderd.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSalesOrderd() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();
        salesOrderd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesOrderdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salesOrderd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salesOrderd))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalesOrderd() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();
        salesOrderd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesOrderdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salesOrderd))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalesOrderd() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderdRepository.findAll().size();
        salesOrderd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesOrderdMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salesOrderd))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesOrderd in the database
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalesOrderd() throws Exception {
        // Initialize the database
        salesOrderdRepository.saveAndFlush(salesOrderd);

        int databaseSizeBeforeDelete = salesOrderdRepository.findAll().size();

        // Delete the salesOrderd
        restSalesOrderdMockMvc
            .perform(delete(ENTITY_API_URL_ID, salesOrderd.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalesOrderd> salesOrderdList = salesOrderdRepository.findAll();
        assertThat(salesOrderdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
