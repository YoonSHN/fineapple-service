package com.fineapple.application.controller.view;



import com.fineapple.application.common.CartUserIdProvider;
import com.fineapple.domain.product.dto.CategoryDto;
import com.fineapple.domain.product.service.CategoryService;
import com.fineapple.domain.product.service.ProductService;
import com.fineapple.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/product")
public class ProductPageController {

    private final ProductService productService;
    private final CartUserIdProvider cartUserIdProvider;
    private final CategoryService categoryService;

    @GetMapping("/{productId}")
    public String findOneProduct(@PathVariable long productId, @AuthenticationPrincipal User user, Model model, HttpServletRequest request) {
        List<CategoryDto> mainCategories = categoryService.getCategory();

        //상품id로 상품 상세 페이지 접근
        model.addAttribute("product", productService.getProductDetail(productId));
        model.addAttribute("productId", productId);
        model.addAttribute("userId", cartUserIdProvider.resolveUserId(user,request));

        model.addAttribute("mainCategories",
                mainCategories == null ? List.of() : mainCategories);

        return "productDetail"; //productDetail.html
    }


}
