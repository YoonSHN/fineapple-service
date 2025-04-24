package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class OrderItemDetailHistory {
    private Long historyId;
    private Long changedBy;
    private Long orderItemDetailId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Integer oldQuantity;
    private Integer newQuantity;
    private String changeReason;
    private String itemHistoryStatus;
    private LocalDateTime changedAt;

    @Builder
    public OrderItemDetailHistory(Long historyId, Long changedBy, Long orderItemDetailId, BigDecimal oldPrice,
                                  BigDecimal newPrice, Integer oldQuantity, Integer newQuantity, String changeReason,
                                  String itemHistoryStatus, LocalDateTime changedAt) {
        this.historyId = historyId;
        this.changedBy = changedBy;
        this.orderItemDetailId = orderItemDetailId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
        this.changeReason = changeReason;
        this.itemHistoryStatus = itemHistoryStatus;
        this.changedAt = changedAt;
    }
}
