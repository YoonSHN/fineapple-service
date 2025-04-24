package com.fineapple.domain.employee.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Department {
    private String departmentId;
    private String name;

    @Builder
    public Department(String departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }
}

