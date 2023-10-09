package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalesOrderdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesOrderd.class);
        SalesOrderd salesOrderd1 = new SalesOrderd();
        salesOrderd1.setId(1L);
        SalesOrderd salesOrderd2 = new SalesOrderd();
        salesOrderd2.setId(salesOrderd1.getId());
        assertThat(salesOrderd1).isEqualTo(salesOrderd2);
        salesOrderd2.setId(2L);
        assertThat(salesOrderd1).isNotEqualTo(salesOrderd2);
        salesOrderd1.setId(null);
        assertThat(salesOrderd1).isNotEqualTo(salesOrderd2);
    }
}
