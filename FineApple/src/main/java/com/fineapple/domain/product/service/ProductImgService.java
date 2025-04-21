package com.fineapple.domain.product.service;

import com.fineapple.domain.product.dto.ProductImageInsertDto;
import com.fineapple.domain.product.dto.ProductInsertDto;
import com.fineapple.domain.product.dto.ProductOptionDto;
import com.fineapple.domain.product.dto.ProductUpdateDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImgService {
    @Transactional
    String upload(ProductInsertDto productDto, ProductImageInsertDto imageDto, MultipartFile file);

    @Transactional
    void insertOption(Long productId, ProductOptionDto productOptionDto);

    @Transactional
    void updateProduct(Long productId, ProductUpdateDto productUpdateDto, MultipartFile file);
}
