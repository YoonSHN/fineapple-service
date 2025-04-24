package com.fineapple.domain.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileHistorySearchParam {

    private Long userId;
    private String fieldChanged;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Min(value = 0, message = "offset은 0 이상")
    private int offset;

    @Min(value = 1, message = "페이지 크기는 1 이상")
    private int pageSize = 10;
}
