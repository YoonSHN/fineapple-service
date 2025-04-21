package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDetailAdminDto {
    private Long orderId;
    private String orderCode;
    private Boolean isCancelled;
    private String orderStatus;
    private String paymentMethod;
    private Long userId;
    private Long guestId;
    private Long orderItemDetailId;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean couponApplied;
    private BigDecimal additionalPrice;
    private String additional;
    private String itemStatus;
    private Long paymentId;
    private String paymentStatus;
    private BigDecimal paymentTotal;
    private String paymentRequestedAt;
    private String paidAt;
    private String paymentCancelledAt;
    private String paymentMethodDetail;
    private Long paymentDetailId;
    private String paymentProductName;
    private BigDecimal paidAmount;
    private Integer paidQuantity;
    private Integer cancelledQuantity;
    private BigDecimal cancelledAmount;
    private String failReason;
    private String paymentDetailCancelledAt;
    private String customerName;
    private String customerTel;
    private String userExtraInfo;
    private Long shipmentId;
    private String trackingNumber;
    private String courierCompany;
    private String shipmentStatus;
    private String dispatchedAt;
    private String deliveredAt;
    private String estimatedDeliveryDate;
    private String shippingAddress;
    private String shippingCity;
    private String shippingRegion;
    private String shippingPostNum;
    private String shippingRoadNum;
    private String receiverName;
    private String receiverTel;
    private String imageUrl;
}
