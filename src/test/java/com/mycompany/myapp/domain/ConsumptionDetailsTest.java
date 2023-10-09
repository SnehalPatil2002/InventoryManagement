package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumptionDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumptionDetails.class);
        ConsumptionDetails consumptionDetails1 = new ConsumptionDetails();
        consumptionDetails1.setId(1L);
        ConsumptionDetails consumptionDetails2 = new ConsumptionDetails();
        consumptionDetails2.setId(consumptionDetails1.getId());
        assertThat(consumptionDetails1).isEqualTo(consumptionDetails2);
        consumptionDetails2.setId(2L);
        assertThat(consumptionDetails1).isNotEqualTo(consumptionDetails2);
        consumptionDetails1.setId(null);
        assertThat(consumptionDetails1).isNotEqualTo(consumptionDetails2);
    }
}
