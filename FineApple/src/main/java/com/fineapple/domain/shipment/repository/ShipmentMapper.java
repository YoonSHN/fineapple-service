package com.fineapple.domain.shipment.repository;

import com.fineapple.domain.shipment.dto.ShipmentDetailDto;
import com.fineapple.domain.shipment.dto.ShipmentDto;
import com.fineapple.domain.shipment.dto.ShipmentDetailDto;
import com.fineapple.domain.shipment.entity.Shipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShipmentMapper {
//
    ShipmentDetailDto findShipmentById(@Param("shipmentId") Long shipmentId);
//
//    void insertShipment(Shipment shipment);
//
//    void updateShipmentStatus(@Param("shipmentId") Long shipmentId, @Param("deliveryStatus") String deliveryStatus);

   List<ShipmentDto> findAllShipments();

}

