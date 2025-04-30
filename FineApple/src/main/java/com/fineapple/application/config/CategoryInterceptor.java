package com.fineapple.application.config;

import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * 전역 컨트롤러에 카테고리 DB정보주입
 */
@Component
@RequiredArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {

    private final CategoryService categoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        if (url.startsWith("/api")) {
            return true;
        }
        List<CategoryDto> mainCategories = categoryService.getCategory();
        request.setAttribute("mainCategories", mainCategories);
        return true;
    }
}
