package com.fineapple.domain.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long cartId;
    private Long quantity = 0L;
    private String productUrl;
    private String productName;
    private BigDecimal productPrice = BigDecimal.ZERO;
    private Long productId;
    private String optionsJson;  // options를 String으로 받음



    // String으로 받은 optionsJson을 Map으로 변환하여 반환
    public Map<String, BigDecimal> getOptions() {
        if (optionsJson != null && !optionsJson.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(optionsJson, Map.class);  // Map으로 변환
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;  // optionsJson이 null 또는 빈 값일 경우
    }

    // 묶음별 가격
    public BigDecimal getSubTotal() {
        BigDecimal baseTotal = BigDecimal.ZERO;

        if (productPrice != null && quantity != null) {
            baseTotal = productPrice.multiply(BigDecimal.valueOf(quantity));
        }

        BigDecimal optionTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> options = getOptions();

//        옵션가격도 포함
        if (options != null) {
            for (BigDecimal optionPrice : options.values()) {
                if (optionPrice != null) {
                    optionTotal = optionTotal.add(optionPrice);
                }
            }
        }
        return baseTotal.add(optionTotal);
    }
}
