package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Warehouse;
import com.mycompany.myapp.repository.WarehouseRepository;
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
 * Integration tests for the {@link WarehouseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarehouseResourceIT {

    private static final String DEFAULT_WH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_PINCODE = 1L;
    private static final Long UPDATED_PINCODE = 2L;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/warehouses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarehouseMockMvc;

    private Warehouse warehouse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createEntity(EntityManager em) {
        Warehouse warehouse = new Warehouse()
            .whName(DEFAULT_WH_NAME)
            .address(DEFAULT_ADDRESS)
            .pincode(DEFAULT_PINCODE)
            .city(DEFAULT_CITY)
            .managerName(DEFAULT_MANAGER_NAME)
            .managerEmail(DEFAULT_MANAGER_EMAIL);
        return warehouse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warehouse createUpdatedEntity(EntityManager em) {
        Warehouse warehouse = new Warehouse()
            .whName(UPDATED_WH_NAME)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .managerName(UPDATED_MANAGER_NAME)
            .managerEmail(UPDATED_MANAGER_EMAIL);
        return warehouse;
    }

    @BeforeEach
    public void initTest() {
        warehouse = createEntity(em);
    }

    @Test
    @Transactional
    void createWarehouse() throws Exception {
        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();
        // Create the Warehouse
        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isCreated());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate + 1);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getWhName()).isEqualTo(DEFAULT_WH_NAME);
        assertThat(testWarehouse.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testWarehouse.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testWarehouse.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testWarehouse.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testWarehouse.getManagerEmail()).isEqualTo(DEFAULT_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void createWarehouseWithExistingId() throws Exception {
        // Create the Warehouse with an existing ID
        warehouse.setId(1L);

        int databaseSizeBeforeCreate = warehouseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarehouseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWarehouses() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get all the warehouseList
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].whName").value(hasItem(DEFAULT_WH_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE.intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].managerEmail").value(hasItem(DEFAULT_MANAGER_EMAIL)));
    }

    @Test
    @Transactional
    void getWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        // Get the warehouse
        restWarehouseMockMvc
            .perform(get(ENTITY_API_URL_ID, warehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warehouse.getId().intValue()))
            .andExpect(jsonPath("$.whName").value(DEFAULT_WH_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE.intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.managerName").value(DEFAULT_MANAGER_NAME))
            .andExpect(jsonPath("$.managerEmail").value(DEFAULT_MANAGER_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingWarehouse() throws Exception {
        // Get the warehouse
        restWarehouseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse
        Warehouse updatedWarehouse = warehouseRepository.findById(warehouse.getId()).get();
        // Disconnect from session so that the updates on updatedWarehouse are not directly saved in db
        em.detach(updatedWarehouse);
        updatedWarehouse
            .whName(UPDATED_WH_NAME)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .managerName(UPDATED_MANAGER_NAME)
            .managerEmail(UPDATED_MANAGER_EMAIL);

        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWarehouse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getWhName()).isEqualTo(UPDATED_WH_NAME);
        assertThat(testWarehouse.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testWarehouse.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testWarehouse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testWarehouse.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testWarehouse.getManagerEmail()).isEqualTo(UPDATED_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warehouse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse.address(UPDATED_ADDRESS);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getWhName()).isEqualTo(DEFAULT_WH_NAME);
        assertThat(testWarehouse.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testWarehouse.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testWarehouse.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testWarehouse.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testWarehouse.getManagerEmail()).isEqualTo(DEFAULT_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateWarehouseWithPatch() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();

        // Update the warehouse using partial update
        Warehouse partialUpdatedWarehouse = new Warehouse();
        partialUpdatedWarehouse.setId(warehouse.getId());

        partialUpdatedWarehouse
            .whName(UPDATED_WH_NAME)
            .address(UPDATED_ADDRESS)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .managerName(UPDATED_MANAGER_NAME)
            .managerEmail(UPDATED_MANAGER_EMAIL);

        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarehouse))
            )
            .andExpect(status().isOk());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
        Warehouse testWarehouse = warehouseList.get(warehouseList.size() - 1);
        assertThat(testWarehouse.getWhName()).isEqualTo(UPDATED_WH_NAME);
        assertThat(testWarehouse.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testWarehouse.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testWarehouse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testWarehouse.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testWarehouse.getManagerEmail()).isEqualTo(UPDATED_MANAGER_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warehouse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = warehouseRepository.findAll().size();
        warehouse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(warehouse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warehouse in the database
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarehouse() throws Exception {
        // Initialize the database
        warehouseRepository.saveAndFlush(warehouse);

        int databaseSizeBeforeDelete = warehouseRepository.findAll().size();

        // Delete the warehouse
        restWarehouseMockMvc
            .perform(delete(ENTITY_API_URL_ID, warehouse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        assertThat(warehouseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
