package com.fineapple.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class Category {
    private Long categoryId;
    private String name;
    private String path;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentId;
    private String visibilityStatus;


    @Builder
    public Category(Long categoryId, String name, String path, String description, LocalDateTime createdAt, LocalDateTime updatedAt, Long parentId, String visibilityStatus) {
        this.categoryId = categoryId;
        this.name = name;
        this.path = path;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentId = parentId;
        this.visibilityStatus = visibilityStatus;
    }
}

