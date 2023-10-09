package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Projects.class);
        Projects projects1 = new Projects();
        projects1.setId(1L);
        Projects projects2 = new Projects();
        projects2.setId(projects1.getId());
        assertThat(projects1).isEqualTo(projects2);
        projects2.setId(2L);
        assertThat(projects1).isNotEqualTo(projects2);
        projects1.setId(null);
        assertThat(projects1).isNotEqualTo(projects2);
    }
}
