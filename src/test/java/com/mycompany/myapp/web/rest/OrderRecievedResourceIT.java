package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OrderRecieved;
import com.mycompany.myapp.repository.OrderRecievedRepository;
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
 * Integration tests for the {@link OrderRecievedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderRecievedResourceIT {

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_OR_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OR_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_QTY_ORDERED = 1L;
    private static final Long UPDATED_QTY_ORDERED = 2L;

    private static final Long DEFAULT_QTY_RECIEVED = 1L;
    private static final Long UPDATED_QTY_RECIEVED = 2L;

    private static final Instant DEFAULT_MANUFACTURING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MANUFACTURING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_QTY_APPROVED = 1L;
    private static final Long UPDATED_QTY_APPROVED = 2L;

    private static final Long DEFAULT_QTY_REJECTED = 1L;
    private static final Long UPDATED_QTY_REJECTED = 2L;

    private static final String ENTITY_API_URL = "/api/order-recieveds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRecievedRepository orderRecievedRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderRecievedMockMvc;

    private OrderRecieved orderRecieved;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderRecieved createEntity(EntityManager em) {
        OrderRecieved orderRecieved = new OrderRecieved()
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .orDate(DEFAULT_OR_DATE)
            .qtyOrdered(DEFAULT_QTY_ORDERED)
            .qtyRecieved(DEFAULT_QTY_RECIEVED)
            .manufacturingDate(DEFAULT_MANUFACTURING_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .qtyApproved(DEFAULT_QTY_APPROVED)
            .qtyRejected(DEFAULT_QTY_REJECTED);
        return orderRecieved;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderRecieved createUpdatedEntity(EntityManager em) {
        OrderRecieved orderRecieved = new OrderRecieved()
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .orDate(UPDATED_OR_DATE)
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .qtyRecieved(UPDATED_QTY_RECIEVED)
            .manufacturingDate(UPDATED_MANUFACTURING_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .qtyApproved(UPDATED_QTY_APPROVED)
            .qtyRejected(UPDATED_QTY_REJECTED);
        return orderRecieved;
    }

    @BeforeEach
    public void initTest() {
        orderRecieved = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderRecieved() throws Exception {
        int databaseSizeBeforeCreate = orderRecievedRepository.findAll().size();
        // Create the OrderRecieved
        restOrderRecievedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRecieved)))
            .andExpect(status().isCreated());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeCreate + 1);
        OrderRecieved testOrderRecieved = orderRecievedList.get(orderRecievedList.size() - 1);
        assertThat(testOrderRecieved.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testOrderRecieved.getOrDate()).isEqualTo(DEFAULT_OR_DATE);
        assertThat(testOrderRecieved.getQtyOrdered()).isEqualTo(DEFAULT_QTY_ORDERED);
        assertThat(testOrderRecieved.getQtyRecieved()).isEqualTo(DEFAULT_QTY_RECIEVED);
        assertThat(testOrderRecieved.getManufacturingDate()).isEqualTo(DEFAULT_MANUFACTURING_DATE);
        assertThat(testOrderRecieved.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testOrderRecieved.getQtyApproved()).isEqualTo(DEFAULT_QTY_APPROVED);
        assertThat(testOrderRecieved.getQtyRejected()).isEqualTo(DEFAULT_QTY_REJECTED);
    }

    @Test
    @Transactional
    void createOrderRecievedWithExistingId() throws Exception {
        // Create the OrderRecieved with an existing ID
        orderRecieved.setId(1L);

        int databaseSizeBeforeCreate = orderRecievedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderRecievedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRecieved)))
            .andExpect(status().isBadRequest());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderRecieveds() throws Exception {
        // Initialize the database
        orderRecievedRepository.saveAndFlush(orderRecieved);

        // Get all the orderRecievedList
        restOrderRecievedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderRecieved.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].orDate").value(hasItem(DEFAULT_OR_DATE.toString())))
            .andExpect(jsonPath("$.[*].qtyOrdered").value(hasItem(DEFAULT_QTY_ORDERED.intValue())))
            .andExpect(jsonPath("$.[*].qtyRecieved").value(hasItem(DEFAULT_QTY_RECIEVED.intValue())))
            .andExpect(jsonPath("$.[*].manufacturingDate").value(hasItem(DEFAULT_MANUFACTURING_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].qtyApproved").value(hasItem(DEFAULT_QTY_APPROVED.intValue())))
            .andExpect(jsonPath("$.[*].qtyRejected").value(hasItem(DEFAULT_QTY_REJECTED.intValue())));
    }

    @Test
    @Transactional
    void getOrderRecieved() throws Exception {
        // Initialize the database
        orderRecievedRepository.saveAndFlush(orderRecieved);

        // Get the orderRecieved
        restOrderRecievedMockMvc
            .perform(get(ENTITY_API_URL_ID, orderRecieved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderRecieved.getId().intValue()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.orDate").value(DEFAULT_OR_DATE.toString()))
            .andExpect(jsonPath("$.qtyOrdered").value(DEFAULT_QTY_ORDERED.intValue()))
            .andExpect(jsonPath("$.qtyRecieved").value(DEFAULT_QTY_RECIEVED.intValue()))
            .andExpect(jsonPath("$.manufacturingDate").value(DEFAULT_MANUFACTURING_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.qtyApproved").value(DEFAULT_QTY_APPROVED.intValue()))
            .andExpect(jsonPath("$.qtyRejected").value(DEFAULT_QTY_REJECTED.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrderRecieved() throws Exception {
        // Get the orderRecieved
        restOrderRecievedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderRecieved() throws Exception {
        // Initialize the database
        orderRecievedRepository.saveAndFlush(orderRecieved);

        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();

        // Update the orderRecieved
        OrderRecieved updatedOrderRecieved = orderRecievedRepository.findById(orderRecieved.getId()).get();
        // Disconnect from session so that the updates on updatedOrderRecieved are not directly saved in db
        em.detach(updatedOrderRecieved);
        updatedOrderRecieved
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .orDate(UPDATED_OR_DATE)
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .qtyRecieved(UPDATED_QTY_RECIEVED)
            .manufacturingDate(UPDATED_MANUFACTURING_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .qtyApproved(UPDATED_QTY_APPROVED)
            .qtyRejected(UPDATED_QTY_REJECTED);

        restOrderRecievedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderRecieved.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderRecieved))
            )
            .andExpect(status().isOk());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
        OrderRecieved testOrderRecieved = orderRecievedList.get(orderRecievedList.size() - 1);
        assertThat(testOrderRecieved.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testOrderRecieved.getOrDate()).isEqualTo(UPDATED_OR_DATE);
        assertThat(testOrderRecieved.getQtyOrdered()).isEqualTo(UPDATED_QTY_ORDERED);
        assertThat(testOrderRecieved.getQtyRecieved()).isEqualTo(UPDATED_QTY_RECIEVED);
        assertThat(testOrderRecieved.getManufacturingDate()).isEqualTo(UPDATED_MANUFACTURING_DATE);
        assertThat(testOrderRecieved.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testOrderRecieved.getQtyApproved()).isEqualTo(UPDATED_QTY_APPROVED);
        assertThat(testOrderRecieved.getQtyRejected()).isEqualTo(UPDATED_QTY_REJECTED);
    }

    @Test
    @Transactional
    void putNonExistingOrderRecieved() throws Exception {
        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();
        orderRecieved.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderRecievedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderRecieved.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderRecieved))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderRecieved() throws Exception {
        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();
        orderRecieved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRecievedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderRecieved))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderRecieved() throws Exception {
        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();
        orderRecieved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRecievedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderRecieved)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderRecievedWithPatch() throws Exception {
        // Initialize the database
        orderRecievedRepository.saveAndFlush(orderRecieved);

        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();

        // Update the orderRecieved using partial update
        OrderRecieved partialUpdatedOrderRecieved = new OrderRecieved();
        partialUpdatedOrderRecieved.setId(orderRecieved.getId());

        partialUpdatedOrderRecieved
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .qtyRecieved(UPDATED_QTY_RECIEVED)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .qtyRejected(UPDATED_QTY_REJECTED);

        restOrderRecievedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderRecieved.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderRecieved))
            )
            .andExpect(status().isOk());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
        OrderRecieved testOrderRecieved = orderRecievedList.get(orderRecievedList.size() - 1);
        assertThat(testOrderRecieved.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testOrderRecieved.getOrDate()).isEqualTo(DEFAULT_OR_DATE);
        assertThat(testOrderRecieved.getQtyOrdered()).isEqualTo(UPDATED_QTY_ORDERED);
        assertThat(testOrderRecieved.getQtyRecieved()).isEqualTo(UPDATED_QTY_RECIEVED);
        assertThat(testOrderRecieved.getManufacturingDate()).isEqualTo(DEFAULT_MANUFACTURING_DATE);
        assertThat(testOrderRecieved.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testOrderRecieved.getQtyApproved()).isEqualTo(DEFAULT_QTY_APPROVED);
        assertThat(testOrderRecieved.getQtyRejected()).isEqualTo(UPDATED_QTY_REJECTED);
    }

    @Test
    @Transactional
    void fullUpdateOrderRecievedWithPatch() throws Exception {
        // Initialize the database
        orderRecievedRepository.saveAndFlush(orderRecieved);

        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();

        // Update the orderRecieved using partial update
        OrderRecieved partialUpdatedOrderRecieved = new OrderRecieved();
        partialUpdatedOrderRecieved.setId(orderRecieved.getId());

        partialUpdatedOrderRecieved
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .orDate(UPDATED_OR_DATE)
            .qtyOrdered(UPDATED_QTY_ORDERED)
            .qtyRecieved(UPDATED_QTY_RECIEVED)
            .manufacturingDate(UPDATED_MANUFACTURING_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .qtyApproved(UPDATED_QTY_APPROVED)
            .qtyRejected(UPDATED_QTY_REJECTED);

        restOrderRecievedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderRecieved.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderRecieved))
            )
            .andExpect(status().isOk());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
        OrderRecieved testOrderRecieved = orderRecievedList.get(orderRecievedList.size() - 1);
        assertThat(testOrderRecieved.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testOrderRecieved.getOrDate()).isEqualTo(UPDATED_OR_DATE);
        assertThat(testOrderRecieved.getQtyOrdered()).isEqualTo(UPDATED_QTY_ORDERED);
        assertThat(testOrderRecieved.getQtyRecieved()).isEqualTo(UPDATED_QTY_RECIEVED);
        assertThat(testOrderRecieved.getManufacturingDate()).isEqualTo(UPDATED_MANUFACTURING_DATE);
        assertThat(testOrderRecieved.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testOrderRecieved.getQtyApproved()).isEqualTo(UPDATED_QTY_APPROVED);
        assertThat(testOrderRecieved.getQtyRejected()).isEqualTo(UPDATED_QTY_REJECTED);
    }

    @Test
    @Transactional
    void patchNonExistingOrderRecieved() throws Exception {
        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();
        orderRecieved.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderRecievedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderRecieved.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderRecieved))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderRecieved() throws Exception {
        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();
        orderRecieved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRecievedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderRecieved))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderRecieved() throws Exception {
        int databaseSizeBeforeUpdate = orderRecievedRepository.findAll().size();
        orderRecieved.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderRecievedMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderRecieved))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderRecieved in the database
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderRecieved() throws Exception {
        // Initialize the database
        orderRecievedRepository.saveAndFlush(orderRecieved);

        int databaseSizeBeforeDelete = orderRecievedRepository.findAll().size();

        // Delete the orderRecieved
        restOrderRecievedMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderRecieved.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderRecieved> orderRecievedList = orderRecievedRepository.findAll();
        assertThat(orderRecievedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
