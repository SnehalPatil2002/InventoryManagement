package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchaseRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseRequest.class);
        PurchaseRequest purchaseRequest1 = new PurchaseRequest();
        purchaseRequest1.setId(1L);
        PurchaseRequest purchaseRequest2 = new PurchaseRequest();
        purchaseRequest2.setId(purchaseRequest1.getId());
        assertThat(purchaseRequest1).isEqualTo(purchaseRequest2);
        purchaseRequest2.setId(2L);
        assertThat(purchaseRequest1).isNotEqualTo(purchaseRequest2);
        purchaseRequest1.setId(null);
        assertThat(purchaseRequest1).isNotEqualTo(purchaseRequest2);
    }
}
