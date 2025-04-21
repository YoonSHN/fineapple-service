package com.fineapple.application.controller.api.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fineapple.Infrastructure.exception.ProductUploadException;
import com.fineapple.domain.product.dto.*;
import com.fineapple.domain.product.service.ProductImgService;
import com.fineapple.domain.product.service.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;
import java.util.Locale;

/**
 * 관리자 전용 상품 관리 API 컨트롤러
 * <p>
 * - 관리자 권한(ROLE_ADMIN)을 가진 사용자만 접근 가능
 * - 상품 목록 조회, 상품 업로드 및 수정, 옵션 추가/삭제, 상태 토글 등의 기능을 제공
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminProductApiController {

    private final ProductImgService productImgService;
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;


    @GetMapping
    @Operation(summary = "상품 목록 조회")
    public ResponseEntity<PageInfo<ProductAdminDto>> getAllProducts(
            @Validated @ModelAttribute ProductSearchParam productSearchParam,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo<ProductAdminDto> productPage = productService.getAllProduct(productSearchParam, pageNum, pageSize);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 옵션 조회")
    public ResponseEntity<List<ProductAdminDto>> getProductDetails(@PathVariable Long productId) {
        List<ProductAdminDto> productPage = productService.getAllProductOptionByproductId(productId);
        return ResponseEntity.ok(productPage);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "업로드")
    public ResponseEntity<String> uploadProductWithImage(
            @RequestPart("product") String productJson,
            @RequestPart("imageDto") String imageJson,
            @RequestPart("file") MultipartFile file,
            Locale locale) {
        ProductInsertDto productDto = null;
        try {
            productDto = objectMapper.readValue(productJson, ProductInsertDto.class);
        } catch (JsonProcessingException e) {
            String message = messageSource.getMessage("error.product.upload.empty_product_json", null, locale);
            throw new ProductUploadException(message);
        }
        ProductImageInsertDto imageDto = null;
        try {
            imageDto = objectMapper.readValue(imageJson, ProductImageInsertDto.class);
        } catch (JsonProcessingException e) {
            String message = messageSource.getMessage("error.product.upload.empty_image_json", null, locale);
            throw new ProductUploadException(message);
        }
        String imageUrl = productImgService.upload(productDto, imageDto, file);
        return ResponseEntity.ok(imageUrl);
    }

    @Operation(summary = "상품 추가 ")
    @PostMapping("/{productId}/options")
    public ResponseEntity<Void> insertProductOption(
            @PathVariable Long productId,
            @RequestBody ProductOptionDto productOptionDto
    ) {
        productImgService.insertOption(productId, productOptionDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품 활성 상태 토글")
    @PatchMapping("/{productId}/toggle-active")
    public ResponseEntity<Void> toggleActive(@PathVariable Long productId) {
        productService.toggleIsActive(productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품 옵션 삭제")
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(
            @PathVariable Long productId,
            @PathVariable Long optionId
    ) {
        productService.deleteOption(productId, optionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상품 수정")
    @PatchMapping(value = "/{productId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long productId,
            @RequestPart("product") String productJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Locale locale
    ) {
        ProductUpdateDto updateDto;
        try {
            updateDto = objectMapper.readValue(productJson, ProductUpdateDto.class);
        } catch (JsonProcessingException e) {
            String message = messageSource.getMessage("error.product.update.invalid_json", null, locale);
            throw new ProductUploadException(message);
        }

        productImgService.updateProduct(productId, updateDto, file);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/{id}")
//    public AdminProductDetailDto getAdminProduct(@PathVariable Long id) {
//        return productService.getAdminProductDetail(id);
//    }
}
