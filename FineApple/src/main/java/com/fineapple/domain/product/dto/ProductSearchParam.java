package com.fineapple.domain.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchParam {

    @Size(max = 100)
    private String name;
    private Boolean isActive;
    private String saleStatus;

    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE, message = "정렬 방향은 asc 또는 desc만 가능합니다.")
    private String sortDir;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Min(value = 0, message = "offset은 0 이상")
    private int offset;

    @Min(value = 1, message = "페이지 크기는 1 이상")
    private int pageSize = 10;

}
