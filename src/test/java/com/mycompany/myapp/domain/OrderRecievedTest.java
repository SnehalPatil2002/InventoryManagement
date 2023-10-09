package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderRecievedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderRecieved.class);
        OrderRecieved orderRecieved1 = new OrderRecieved();
        orderRecieved1.setId(1L);
        OrderRecieved orderRecieved2 = new OrderRecieved();
        orderRecieved2.setId(orderRecieved1.getId());
        assertThat(orderRecieved1).isEqualTo(orderRecieved2);
        orderRecieved2.setId(2L);
        assertThat(orderRecieved1).isNotEqualTo(orderRecieved2);
        orderRecieved1.setId(null);
        assertThat(orderRecieved1).isNotEqualTo(orderRecieved2);
    }
}
