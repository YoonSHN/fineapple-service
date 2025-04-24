package com.fineapple.domain.product.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CategoryDto {
    private Long categoryId;
    private String name;
    private String path;
    private String description;
    private Long parentId;
    private String visibilityStatus;
    private Integer depth;
}

