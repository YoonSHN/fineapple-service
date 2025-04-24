package com.fineapple.domain.employee.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class EmployeeInfo {
    private Long empInfo;
    private Long employeeId;
    private String departmentId;
    private String title;
    private BigDecimal salary;
    private Integer vacationCount;
    private Boolean requiredTraining;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long storeId;
    private String employeeStatus;

    @Builder
    public EmployeeInfo(Long empInfo, Long employeeId, String departmentId, String title, BigDecimal salary,
                        Integer vacationCount, Boolean requiredTraining, LocalDateTime createdAt, LocalDateTime updatedAt,
                        Long storeId, String employeeStatus) {
        this.empInfo = empInfo;
        this.employeeId = employeeId;
        this.departmentId = departmentId;
        this.title = title;
        this.salary = salary;
        this.vacationCount = vacationCount;
        this.requiredTraining = requiredTraining;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.storeId = storeId;
        this.employeeStatus = employeeStatus;
    }
}
