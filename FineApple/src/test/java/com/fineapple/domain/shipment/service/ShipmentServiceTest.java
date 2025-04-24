package com.fineapple.domain.shipment.service;

import com.fineapple.domain.shipment.dto.ShipmentDetailDto;
import com.fineapple.domain.shipment.repository.ShipmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ShipmentServiceTest {


    @Autowired
    private ShipmentService shipmentService;
    @Test
    void findAllShipments() {
    }

    @Test
    void findShipmentDetailById() {
        ShipmentDetailDto detail =shipmentService.findShipmentDetailById(1001L);
        assertNotNull(detail);
        assertNotNull(detail.getShipmentId());
        assertEquals(1001L, detail.getShipmentId());
        System.out.println(detail.getOrderStatusName());

    }
}