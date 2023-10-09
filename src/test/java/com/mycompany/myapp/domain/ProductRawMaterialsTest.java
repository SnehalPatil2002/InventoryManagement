package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductRawMaterialsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductRawMaterials.class);
        ProductRawMaterials productRawMaterials1 = new ProductRawMaterials();
        productRawMaterials1.setId(1L);
        ProductRawMaterials productRawMaterials2 = new ProductRawMaterials();
        productRawMaterials2.setId(productRawMaterials1.getId());
        assertThat(productRawMaterials1).isEqualTo(productRawMaterials2);
        productRawMaterials2.setId(2L);
        assertThat(productRawMaterials1).isNotEqualTo(productRawMaterials2);
        productRawMaterials1.setId(null);
        assertThat(productRawMaterials1).isNotEqualTo(productRawMaterials2);
    }
}
