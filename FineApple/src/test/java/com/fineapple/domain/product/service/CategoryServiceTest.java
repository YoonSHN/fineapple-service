package com.fineapple.domain.product.service;

import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.repository.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getCategory_정상호출() {
        // given
        List<CategoryDto> mockList = List.of(
                CategoryDto.builder()
                        .categoryId(1L)
                        .name("파인북")
                        .path("finemac")
                        .description("루트")
                        .parentId(null)
                        .depth(0)
                        .build()
        );

        when(categoryMapper.selectCategory()).thenReturn(mockList);

        // when
        List<CategoryDto> result = categoryService.getCategory();

        // then
        assertThat(result).isEqualTo(mockList);
        verify(categoryMapper, times(1)).selectCategory();
    }

    @Test
    void getCategoryByPath_정상호출() {
        // given
        String path = "finemac";
        List<CategoryDto> mockList = List.of(
                CategoryDto.builder()
                        .categoryId(2L)
                        .name("파인북 프로")
                        .path("finemac")
                        .description("자식")
                        .parentId(1L)
                        .depth(1)
                        .build()
        );

        when(categoryMapper.selectCategoryTreeByPath(path)).thenReturn(mockList);

        // when
        List<CategoryDto> result = categoryService.getCategoryByPath(path);

        // then
        assertThat(result).isEqualTo(mockList);
        verify(categoryMapper, times(1)).selectCategoryTreeByPath(path);
    }
}
