package com.fineapple.domain.order_payment.dto;

import com.fineapple.domain.logistics_inventory.entity.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderItemDetailUserDto {

    private Long productId;
    private Long optionId;

    private String itemName;

    private Integer itemQuantity;
    private BigDecimal itemPrice;

    private BigDecimal itemDiscountPrice;

}
