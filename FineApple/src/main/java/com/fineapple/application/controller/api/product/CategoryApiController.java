package com.fineapple.application.controller.api.product;

import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 카테고리 관련 API 컨트롤러
 * <p>
 * - 프론트엔드에서 사용할 카테고리 트리를 제공
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryApiController {
    private final CategoryService categoryService;

    @Operation(summary = "경로에 해당하는 카테고리 메뉴 조회")
    @GetMapping("/tree")
    public List<CategoryDto> getCategoryTree(String path) {
        return categoryService.getCategoryByPath(path);
    }

    @Operation(summary = "카테고리 메뉴 조회")
    @GetMapping()
    public List<CategoryDto> getCategoryFindAll() {
        return categoryService.getAllCategories();
    }
}
