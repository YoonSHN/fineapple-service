package com.fineapple.domain.logistics_inventory.service;

import com.fineapple.domain.logistics_inventory.dto.StockDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;
    @Test
    void getStockFromStoreAllProduct() {
        List<StockDto> list = stockService.getStockFromStoreAllProduct(1L);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
}