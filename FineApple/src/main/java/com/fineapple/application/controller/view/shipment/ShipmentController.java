package com.fineapple.application.controller.view.shipment;

import com.fineapple.domain.shipment.dto.ShipmentDetailDto;
import com.fineapple.domain.shipment.dto.ShipmentDto;
import com.fineapple.domain.shipment.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping("/shipment")
    public String shipmentPage(Model model) {

        //전체 배송 정보 조회
        List<ShipmentDto> list = shipmentService.findAllShipments();
        model.addAttribute("list", list);
        return "shipment"; // templates/fragments/shipment.html 반환
    }

    @GetMapping("/shipment/{shipmentId}")
    public String findShipmentDetailById(@PathVariable Long shipmentId, Model model){
        //조금 더 상세한 배송 정보 표시
        ShipmentDetailDto detail = shipmentService.findShipmentDetailById(shipmentId);
        model.addAttribute("detail", detail);

        return "shipmentDetail";
    }
//    @GetMapping("/")
//    public ResponseEntity<ShipmentDto> getShipmentDetail(@PathVariable Long shipmentId){
//        return ResponseEntity.ok(shipmentService.getShipmentDetail(shipmentId));
//    }
//
//    @GetMapping("/shipments") //전체 조회
//    public ResponseEntity<List<ShipmentDto>> getAllShipments() {
//        return ResponseEntity.ok(shipmentService.findAllShipments());
//    }
//
//    @PostMapping("/api/v1/shipments")
//    public ResponseEntity<Void> createShipment(@RequestBody ShipmentRequestDto dto) {
//        shipmentService.createShipment(dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/api/v1/shipments/{shipmentId}")
//    public ResponseEntity<Void> updateShipmentStatus(
//            @PathVariable Long shipmentId,
//            @RequestBody UpdateShipmentStatusRequest request) {
//
//        shipmentService.updateShipmentStatus(shipmentId, request.getDeliveryStatus());
//        return ResponseEntity.ok().build();
//    }
//

}



