package com.fineapple.application.controller.view.user;

import com.fineapple.application.util.CartUserIdProvider;
import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.service.CategoryService;
import com.fineapple.domain.user.dto.CartDto;


import com.fineapple.domain.user.entity.User;
import com.fineapple.domain.user.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/carts")
@AllArgsConstructor
public class CartPageController {

    private final CartService cartService;
    private final CartUserIdProvider cartUserIdProvider;
    private final CategoryService categoryService;

    // 장바구니 페이지 조회
    @GetMapping
    public String getCartPage(HttpServletRequest request, Model model, @AuthenticationPrincipal User user) {

        //회원,비회원 구분
        Long userId = cartUserIdProvider.resolveUserId(user, request);
        List<CategoryDto> mainCategories = categoryService.getCategory();

        CartDto cartDto = cartService.getCart(userId);
        model.addAttribute("cart", cartDto);
        model.addAttribute("userId", userId);

        model.addAttribute("mainCategories",
                mainCategories == null ? List.of() : mainCategories);

        return "cart"; // cart.html 렌더링
    }
}
