package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Projects;
import com.mycompany.myapp.repository.ProjectsRepository;
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
 * Integration tests for the {@link ProjectsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectsResourceIT {

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ORDER_QUANTITY = 1L;
    private static final Long UPDATED_ORDER_QUANTITY = 2L;

    private static final Double DEFAULT_FINAL_TOTAL = 1D;
    private static final Double UPDATED_FINAL_TOTAL = 2D;

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectsMockMvc;

    private Projects projects;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projects createEntity(EntityManager em) {
        Projects projects = new Projects()
            .projectName(DEFAULT_PROJECT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .orderQuantity(DEFAULT_ORDER_QUANTITY)
            .finalTotal(DEFAULT_FINAL_TOTAL);
        return projects;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projects createUpdatedEntity(EntityManager em) {
        Projects projects = new Projects()
            .projectName(UPDATED_PROJECT_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .finalTotal(UPDATED_FINAL_TOTAL);
        return projects;
    }

    @BeforeEach
    public void initTest() {
        projects = createEntity(em);
    }

    @Test
    @Transactional
    void createProjects() throws Exception {
        int databaseSizeBeforeCreate = projectsRepository.findAll().size();
        // Create the Projects
        restProjectsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isCreated());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate + 1);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testProjects.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProjects.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProjects.getOrderQuantity()).isEqualTo(DEFAULT_ORDER_QUANTITY);
        assertThat(testProjects.getFinalTotal()).isEqualTo(DEFAULT_FINAL_TOTAL);
    }

    @Test
    @Transactional
    void createProjectsWithExistingId() throws Exception {
        // Create the Projects with an existing ID
        projects.setId(1L);

        int databaseSizeBeforeCreate = projectsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get all the projectsList
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projects.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].finalTotal").value(hasItem(DEFAULT_FINAL_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        // Get the projects
        restProjectsMockMvc
            .perform(get(ENTITY_API_URL_ID, projects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projects.getId().intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.orderQuantity").value(DEFAULT_ORDER_QUANTITY.intValue()))
            .andExpect(jsonPath("$.finalTotal").value(DEFAULT_FINAL_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingProjects() throws Exception {
        // Get the projects
        restProjectsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects
        Projects updatedProjects = projectsRepository.findById(projects.getId()).get();
        // Disconnect from session so that the updates on updatedProjects are not directly saved in db
        em.detach(updatedProjects);
        updatedProjects
            .projectName(UPDATED_PROJECT_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .finalTotal(UPDATED_FINAL_TOTAL);

        restProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjects.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjects))
            )
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProjects.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProjects.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProjects.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testProjects.getFinalTotal()).isEqualTo(UPDATED_FINAL_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projects.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectsWithPatch() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects using partial update
        Projects partialUpdatedProjects = new Projects();
        partialUpdatedProjects.setId(projects.getId());

        partialUpdatedProjects.projectName(UPDATED_PROJECT_NAME).orderQuantity(UPDATED_ORDER_QUANTITY);

        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjects))
            )
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProjects.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProjects.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testProjects.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testProjects.getFinalTotal()).isEqualTo(DEFAULT_FINAL_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateProjectsWithPatch() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();

        // Update the projects using partial update
        Projects partialUpdatedProjects = new Projects();
        partialUpdatedProjects.setId(projects.getId());

        partialUpdatedProjects
            .projectName(UPDATED_PROJECT_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .finalTotal(UPDATED_FINAL_TOTAL);

        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjects))
            )
            .andExpect(status().isOk());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
        Projects testProjects = projectsList.get(projectsList.size() - 1);
        assertThat(testProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testProjects.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProjects.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testProjects.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testProjects.getFinalTotal()).isEqualTo(UPDATED_FINAL_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projects))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjects() throws Exception {
        int databaseSizeBeforeUpdate = projectsRepository.findAll().size();
        projects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projects)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projects in the database
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjects() throws Exception {
        // Initialize the database
        projectsRepository.saveAndFlush(projects);

        int databaseSizeBeforeDelete = projectsRepository.findAll().size();

        // Delete the projects
        restProjectsMockMvc
            .perform(delete(ENTITY_API_URL_ID, projects.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projects> projectsList = projectsRepository.findAll();
        assertThat(projectsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
