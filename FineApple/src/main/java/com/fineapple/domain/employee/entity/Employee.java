package com.fineapple.domain.employee.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Employee {
    private Long employeeId;
    private String name;
    private Date birth;
    private String tel;
    private String email;
    private String address;
    private Date startDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Employee(Long employeeId, String name, Date birth, String tel, String email, String address, Date startDate,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.employeeId = employeeId;
        this.name = name;
        this.birth = birth;
        this.tel = tel;
        this.email = email;
        this.address = address;
        this.startDate = startDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
