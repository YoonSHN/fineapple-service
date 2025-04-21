package com.fineapple.domain.order_payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class OrderSearchParam {

    private Long orderCode;

    private Long orderId;

    @Size( max = 20)
    private String tel;

    private String orderStatus;

    @JsonProperty("isCancelled")
    private Boolean isCancelled;

    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE, message = "정렬 방향은 asc 또는 desc만 가능합니다.")
    private String sortDir;

    @Min(value = 0, message = "offset은 0 이상")
    private int offset;

    @Min(value = 1, message = "페이지 크기는 1 이상")
    private int pageSize = 10;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
