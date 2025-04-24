package com.fineapple.application.controller.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fineapple.application.common.CartUserIdProvider;
import com.fineapple.domain.product.dto.AdminProductDetailDto;
import com.fineapple.domain.product.dto.ProductDetailDto;
import com.fineapple.domain.product.dto.ProductListDto;
import com.fineapple.domain.product.service.ProductService;
import com.fineapple.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@Slf4j
public class ProductApiController {

    private ProductService productService;
    private CartUserIdProvider cartUserIdProvider;
    //    단건조회
    @GetMapping("/{id}")
    public ProductDetailDto getProduct(@PathVariable long id, @AuthenticationPrincipal User user, HttpServletRequest request) {
//        String userId = principal != null ? principal.getName() : "Guest"; // guest는 알아서 넣어주세요~
        Long userId = cartUserIdProvider.resolveUserId(user,request);
        MDC.put("userId", userId);
        MDC.put("productId", String.valueOf(id));
        MDC.put("action", "view");
        MDC.put("uri", request.getRequestURI());
        MDC.put("userAgent", request.getHeader("User-Agent"));
        MDC.put("eventType", "product_access");

        log.info("product access");
        MDC.clear();
        return productService.getProductDetail(id);
    }

    //    전체조회
    @GetMapping
    public List<ProductListDto> findAllProduct(){
        return productService.getAllProduct();
    }

}