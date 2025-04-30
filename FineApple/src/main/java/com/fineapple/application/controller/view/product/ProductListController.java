package com.fineapple.application.controller.view.product;


import com.fineapple.domain.product.dto.ProductListDto;
import com.fineapple.domain.product.service.CategoryService;
import com.fineapple.domain.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 상품 목록 페이지를 처리하는 컨트롤러
 * <p>
 * productList.html 반환
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class ProductListController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/path/{path}")
    public String showMainCategoryProductList(
            Model model,
            @PathVariable String path,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        PageInfo<ProductListDto> pageInfo = productService.getProductsByCategoryPath(path, pageNum, pageSize);

        model.addAttribute("products", pageInfo.getList());
        // 페이징은 일단 미사용할예정 추후 고려
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("path", path);
        model.addAttribute("mainCategories", categoryService.getCategory());

        return "productList";

    }

    @GetMapping("/id/{categoryId}")
    public String showProductList(
            Model model,
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        PageInfo<ProductListDto> pageInfo = productService.getProductsByCategoryId(categoryId, pageNum, pageSize);

        model.addAttribute("products", pageInfo.getList());
        // 페이징은 일단 미사용할예정 추후 고려
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("mainCategories", categoryService.getCategory());

        return "productList";

    }


}
