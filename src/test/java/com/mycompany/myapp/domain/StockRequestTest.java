package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockRequest.class);
        StockRequest stockRequest1 = new StockRequest();
        stockRequest1.setId(1L);
        StockRequest stockRequest2 = new StockRequest();
        stockRequest2.setId(stockRequest1.getId());
        assertThat(stockRequest1).isEqualTo(stockRequest2);
        stockRequest2.setId(2L);
        assertThat(stockRequest1).isNotEqualTo(stockRequest2);
        stockRequest1.setId(null);
        assertThat(stockRequest1).isNotEqualTo(stockRequest2);
    }
}
