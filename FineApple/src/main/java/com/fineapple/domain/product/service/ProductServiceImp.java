package com.fineapple.domain.product.service;

import com.fineapple.application.exception.ProductNotFoundException;
import com.fineapple.application.exception.ProductUploadException;
import com.fineapple.domain.product.dto.*;
import com.fineapple.domain.product.repository.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
/**
 * 상품 정보 조회 및 관리 기능을 제공하는 서비스 구현체
 *
 * - 관리자 및 사용자 페이지에서 상품 목록, 상세 정보, 옵션 목록 등을 조회하는 데 사용
 * - 상품 상태 토글(활성/비활성), 옵션 삭제 등 관리자 기능도 포함
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductMapper productMapper;
    private final MessageSource messageSource;

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, java.util.Locale.getDefault());
    }

    /**
     * 페이징 기반 상품 목록 조회
     *
     * @param pageNum            페이지 숫자
     * @param pageSize           페이지 크기
     * @param productSearchParam 검색정보
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<ProductAdminDto> getAllProduct(ProductSearchParam productSearchParam, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductAdminDto> productAdminDtos = productMapper.selectAllProduct(productSearchParam);
        return new PageInfo<>(productAdminDtos);
    }

    /**
     * 상품 단건 조회
     *
     * @param productId 상품 아이디
     */
    @Override
    public ProductDetailDto getProductDetail(Long productId) {
        if (!productMapper.existsProductById(productId)) {
            throw new ProductNotFoundException(
                    getMessage("error.product.not_found", productId)
            );
        }
        ProductDetailDto productDetail = productMapper.findProductById(productId);
        if (productDetail == null) {
            throw new ProductNotFoundException(
                    getMessage("error.product.not_found", productId)
            );
        }
        return productDetail;
    }

    // 전체 조회 (paging 처리 해야함)
    @Override
    public List<ProductListDto> getAllProduct() {
        return productMapper.findAllProducts();
    }

    /**
     * .
     * 상품 상세 옵션 목록 조회
     *
     * @param productId 상품아이디
     * @return ProductAdminDto
     * @throws ProductNotFoundException 해당 상품이 존재하지 않을 경우
     */
    @Override
    public List<ProductAdminDto> getAllProductOptionByproductId(Long productId) {
        if (!productMapper.existsProductById(productId)) {
            throw new ProductNotFoundException(
                    getMessage("error.product.not_found", productId)
            );
        }


        return productMapper.selectAllProductWithOption(productId);
    }


    /**
     * 상품 상세조회
     *
     * @param productId 상품아이디
     * @throws ProductNotFoundException 해당 상품이 존재하지 않을 경우
     */
    @Override
    public AdminProductDetailDto getAdminProductDetail(Long productId) {
        if (!productMapper.existsProductById(productId)) {
            throw new ProductNotFoundException(
                    getMessage("error.product.not_found", productId)
            );
        }

        AdminProductDetailDto dto = productMapper.findAdminProductById(productId);
        if (dto == null) {
            throw new ProductNotFoundException(
                    getMessage("error.product.not_found", productId)
            );
        }
        return new AdminProductDetailDto(
                dto.getProductId(),
                defaultIfNull(dto.getProductImage(), ""),
                defaultIfNull(dto.getName(), ""),
                defaultIfNull(dto.getDescription(), ""),
                defaultIfNull(dto.getPrice(), BigDecimal.ZERO),
                defaultIfNull(dto.getCreatedAt(), ""),
                defaultIfNull(dto.getUpdatedAt(), ""),
                defaultIfNull(dto.getCategoryName(), ""),
                defaultIfNull(dto.getIsActive(), false),
                defaultIfNull(dto.getTargetReleaseDate(), "미정"),
                defaultIfNull(dto.getActualReleaseDate(), "미정"),
                defaultIfNull(dto.getSaleStartDate(), "미정"),
                defaultIfNull(dto.getSaleStopDate(), "미정"),
                defaultIfNull(dto.getSaleRestartDate(), "미정"),
                defaultIfNull(dto.getSaleEndDate(), "미정"),
                defaultIfNull(dto.getSaleStatus(), "")
        );
    }

    /**
     * 해당 카테고리에 해당하는 상품리스트 정보를 반환
     *
     * @param categoryId 카테고리 아이디
     * @param pageNum    페이지 숫자
     * @param pageSize   페이지 사이즈
     * @return PageInfo<ProductListDto> 리스트 정보
     */
    @Override
    public PageInfo<ProductListDto> getProductsByCategoryId(Long categoryId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductListDto> list = productMapper.selectProductByCategoryId(categoryId);
        return new PageInfo<>(list);
    }

    /**
     * 메인 카테고리에 해당하는 상품리스트 정보를 반환
     *
     * @param path       카테고리 경로
     * @param pageNum    페이지 숫자
     * @param pageSize   페이지 사이즈
     * @return PageInfo<ProductListDto> 리스트 정보
     */
    @Override
    public PageInfo<ProductListDto> getProductsByCategoryPath(String path, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductListDto> list = productMapper.selectProductByCategoryPath(path);
        return new PageInfo<>(list);
    }

    /**
     * 상품 상태 토글 변경
     *
     * @param productId 상품 아이디
     * @throws ProductNotFoundException 상품이 존재 하지 않거나 업데이트 실패 시
     * @throws ProductUploadException   DB 접근 중 오류가 발생한 경우
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, timeout = 10)
    @Override
    public void toggleIsActive(Long productId) {
        try {
            ProductDetailDto productDetailDto = productMapper.findProductByIdForUpdate(productId);
            if (productDetailDto == null) {
                throw new ProductNotFoundException(getMessage("error.product.not_found", productId));
            }

            boolean toggled = !productDetailDto.isActive();
            int updated = productMapper.updateIsActive(productId, toggled);
            if (updated <= 0) {
                throw new ProductNotFoundException(
                        getMessage("error.product.not_found", productId)
                );
            }

        } catch (DataAccessException e) {
            log.error("상품 상태 변경 중 db 오류 발생함: productId={}", productId, e);
            throw new ProductUploadException(
                    getMessage("error.product.update_failed", productId)
            );
        }
    }

    /**
     * 상품 옵션 삭제
     *
     * @param productId 상품 아이디
     * @param optionId  옵션 아이디
     * @throws ProductUploadException 상품 또는 옵션이 존재하지 않거나 삭제 실패 시
     */
    @Transactional
    @Override
    public void deleteOption(Long productId, Long optionId) {
        ProductOptionDto option = productMapper.findOptionById(optionId);
        if (option == null || !productId.equals(option.getProductId())) {
            throw new ProductUploadException(
                    getMessage("error.product.not_found", productId)
            );
        }

        int deleted = productMapper.deleteProductOptionById(optionId);
        if (deleted <= 0) {
            throw new ProductUploadException(
                    getMessage("error.product.failed", optionId)
            );
        }
    }


}