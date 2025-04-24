package com.fineapple.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MeessageTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    public void testMessage() {
        String message = messageSource.getMessage("error.product.not_found", new Object[]{1L}, Locale.getDefault());
        assertEquals("존재하지 않는 상품입니다. (productId=1)", message);

    }
}
