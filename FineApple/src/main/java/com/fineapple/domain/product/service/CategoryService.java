package com.fineapple.domain.product.service;

import com.fineapple.application.exception.NotFoundException;
import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.repository.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * 카테고리 조회 관련 비즈니스 로직을 담당하는 서비스
 * <p>
 * - 메인 카테고리 목록 및 특정 경로 기반의 카테고리 트리 구조를 제공
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final MessageSource messageSource;


    /**
     * 전체 카테고리 리스트를 반환
     * @return List<CategoryDto>
     */
    public List<CategoryDto> getAllCategories() {
        return categoryMapper.selectAll();
    }

    /**
     * 카테고리 리스트를 반환
     *
     * @return List<CategoryDto>
     */
    @Cacheable(value = "mainCategories")
    public List<CategoryDto> getCategory() {
        List<CategoryDto> categories = categoryMapper.selectCategory();
        if (categories.isEmpty()) {
            throw new NotFoundException(messageSource.getMessage("category.not.found", null, Locale.getDefault()));
        }
        return categories;
    }

    /**
     * 해당 경로에 해당하는 서브 메뉴 트리 리스트 반환 1 -> 2 -> 3
     *
     * @param path 경로
     * @return List<CategoryDto>
     */
    @Cacheable(value = "categoryTree", key = "#path")
    public List<CategoryDto> getCategoryByPath(String path) {
        List<CategoryDto> categoryDtos = categoryMapper.selectCategoryTreeByPath(path);
        if (categoryDtos.isEmpty()) {
            throw new NotFoundException(messageSource.getMessage("category.not.found", null, Locale.getDefault()));
        }
        return categoryDtos;
    }

}
