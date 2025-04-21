package com.fineapple.application.controller;


import com.fineapple.application.common.CartUserIdProvider;
import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.service.CategoryService;
import com.fineapple.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 홈 페이지를 처리하는 컨트롤러
 * <p>
 * 메인 카테고리 목록을 조회하여 index 뷰에 전달
 * "/" 또는 빈 문자열로 요청되는 루트 경로에 매핑
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final CategoryService categoryService;
    private final CartUserIdProvider cartUserIdProvider;

    @GetMapping({"", "/"})
    public String showHomePage(Model model, @AuthenticationPrincipal User user, HttpServletRequest request) {

        Long userId = cartUserIdProvider.resolveUserId(user, request);

        List<CategoryDto> mainCategories = categoryService.getCategory();
        model.addAttribute("mainCategories",
                mainCategories == null ? List.of() : mainCategories);
        model.addAttribute("userId", userId);
        return "index";
    }
}
