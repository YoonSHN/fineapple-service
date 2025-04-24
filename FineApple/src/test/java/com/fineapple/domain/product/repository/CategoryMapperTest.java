package com.fineapple.domain.product.repository;

import com.fineapple.domain.product.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("루트 카테고리 조회 성공")
    void selectCategory_정상조회() {
        // when
        List<CategoryDto> result = categoryMapper.selectCategory();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getParentId()).isNull();
    }

    @Test
    @DisplayName("카테고리 트리 조회 성공")
    void selectCategoryTreeByPath_정상조회() {
        // given
        String path = "finephone";

        // when
        List<CategoryDto> tree = categoryMapper.selectCategoryTreeByPath(path);
        // then
        assertThat(tree).isNotEmpty();
    }
}
