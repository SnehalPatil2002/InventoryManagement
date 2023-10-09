package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchaseQuotationDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseQuotationDetails.class);
        PurchaseQuotationDetails purchaseQuotationDetails1 = new PurchaseQuotationDetails();
        purchaseQuotationDetails1.setId(1L);
        PurchaseQuotationDetails purchaseQuotationDetails2 = new PurchaseQuotationDetails();
        purchaseQuotationDetails2.setId(purchaseQuotationDetails1.getId());
        assertThat(purchaseQuotationDetails1).isEqualTo(purchaseQuotationDetails2);
        purchaseQuotationDetails2.setId(2L);
        assertThat(purchaseQuotationDetails1).isNotEqualTo(purchaseQuotationDetails2);
        purchaseQuotationDetails1.setId(null);
        assertThat(purchaseQuotationDetails1).isNotEqualTo(purchaseQuotationDetails2);
    }
}
