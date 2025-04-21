package com.fineapple.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;




@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductInsertDto {

    private Long productId;

    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private Boolean isActive;
    
    private LocalDateTime targetReleaseDate;
    
    private LocalDateTime actualReleaseDate;
    
    private LocalDateTime saleStartDate;
    
    private LocalDateTime saleStopDate;
    
    private LocalDateTime saleRestartDate;
    
    private LocalDateTime saleEndDate;
    private String saleStatus;
}
