package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPrice.class);
        ProductPrice productPrice1 = new ProductPrice();
        productPrice1.setId(1L);
        ProductPrice productPrice2 = new ProductPrice();
        productPrice2.setId(productPrice1.getId());
        assertThat(productPrice1).isEqualTo(productPrice2);
        productPrice2.setId(2L);
        assertThat(productPrice1).isNotEqualTo(productPrice2);
        productPrice1.setId(null);
        assertThat(productPrice1).isNotEqualTo(productPrice2);
    }
}
