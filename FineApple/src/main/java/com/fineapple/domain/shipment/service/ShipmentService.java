package com.fineapple.domain.shipment.service;


import com.fineapple.application.common.CommonCodeService;
import com.fineapple.domain.order_payment.service.OrderService;
import com.fineapple.domain.shipment.dto.ShipmentDetailDto;
import com.fineapple.domain.shipment.dto.ShipmentDto;
import com.fineapple.domain.shipment.dto.ShipmentDetailDto;
import com.fineapple.domain.shipment.entity.Shipment;
import com.fineapple.domain.shipment.repository.ShipmentMapper;
import com.fineapple.domain.user.entity.Address;
import com.fineapple.domain.user.repository.AddressMapper;
import jakarta.persistence.criteria.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ShipmentService {


    private final ShipmentMapper shipmentMapper;
    private final AddressMapper addressMapper;

    private final CommonCodeService commonCodeService;
    private final OrderService orderService;

    /*
    회원의 모든 배송 정보를 확인 할 수 있는 메서드
     */
    public List<ShipmentDto> findAllShipments(){
        return shipmentMapper.findAllShipments();
    }

    /*
    배송 상세 정보를 출력하는 페이지
    paymentMethod, orderStatus -> 공통코드 (code, 사용자가 알아보기 힘듬)
    -> (code_name 으로 변경)
     */
    public ShipmentDetailDto findShipmentDetailById(Long shipmentId){


        //배송Id 를 기준으로 배송 상세정보 조회
        ShipmentDetailDto detail = shipmentMapper.findShipmentById(shipmentId);

        //공통코드 테이블에서 코드값이 아닌 코드이름 가져옴
        String paymentName = commonCodeService.getCommonCodeName(detail.getPaymentMethod());String orderStatusName = commonCodeService.getCommonCodeName(detail.getOrderStatus());

        detail.setPaymentName(paymentName);
        detail.setOrderStatusName(orderStatusName);

        return detail;
    }
//    public ShipmentDto getShipmentDetail(Long shipmentId) {
//        return shipmentMapper.findShipmentById(shipmentId);
//    }
//
//
//    public void createShipment(ShipmentRequestDto dto) {
//        // Step 1: 배송지 정보 조회
//        Address address = addressMapper.findById(dto.getDeliveryId());
//
//        if (address == null) {
//            throw new IllegalArgumentException("해당 배송지(deliveryId)에 대한 주소 정보가 없습니다.");
//        }
//
//        // Step 2: Shipment Entity로 변환
//        Shipment shipment = Shipment.builder()
//                .trackingNumber("TRK123450") // 초기엔 null
//                .trackingUrl("http://tracking.url/1001")
//                .estimatedDeliveryDate(dto.getEstimatedDeliveryDate())
//                .dispatchedAt(dto.getDispatchedAt())
//                .deliveredAt(dto.getDeliveredAt())
//                .updatedAt(LocalDateTime.now())
//                .deliveryStatus(dto.getDeliveryStatus())
//                .storeId(dto.getStoreId())
//                .deliveryId(dto.getDeliveryId())
//                .shippingCost(dto.getShippingCost())
//                .courierCompany(dto.getCourierCompany())
//                .delayReason(dto.getDelayReason())
//                .deliveryAgentName(dto.getDeliveryAgentName())
//                .deliveryAgentContact(dto.getDeliveryAgentContact())
//
//                // 💡 AddressMapper에서 채워넣기
//                .postNum(address.getPostNum())
//                .city(address.getCity())
//                .region(address.getRegion())
//                .roadNum(address.getRoadNum())
//                .address(address.getAddress())
//
//                .storeName(dto.getStoreName())
//                .orderId(dto.getOrderId())
//                .build();
//
//        // Step 3: Insert
//        shipmentMapper.insertShipment(shipment);
//    }
//
//    private String callCourierApiForTrackingNumber(String courierCompany) {
//        // 실제로는 외부 API 호출
//        return "TRK123456789";
//    }
//
//    private String generateTrackingUrl(String courierCompany, String trackingNumber) {
//        return "https://tracking." + courierCompany.toLowerCase() + ".com/track/" + trackingNumber;
//    }
//    public void updateShipmentStatus(Long shipmentId, String deliveryStatus) {
//        shipmentMapper.updateShipmentStatus(shipmentId, deliveryStatus);
//    }


}
