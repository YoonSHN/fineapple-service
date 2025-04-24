package com.fineapple.domain.product.repository;

import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.entity.Category;
import org.apache.ibatis.annotations.Case;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("select * from Category")
    List<CategoryDto> selectAll();

    List<CategoryDto> selectCategory();

    List<CategoryDto> selectCategoryTreeByPath(String path);

}
