package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RawMaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawMaterial.class);
        RawMaterial rawMaterial1 = new RawMaterial();
        rawMaterial1.setId(1L);
        RawMaterial rawMaterial2 = new RawMaterial();
        rawMaterial2.setId(rawMaterial1.getId());
        assertThat(rawMaterial1).isEqualTo(rawMaterial2);
        rawMaterial2.setId(2L);
        assertThat(rawMaterial1).isNotEqualTo(rawMaterial2);
        rawMaterial1.setId(null);
        assertThat(rawMaterial1).isNotEqualTo(rawMaterial2);
    }
}
