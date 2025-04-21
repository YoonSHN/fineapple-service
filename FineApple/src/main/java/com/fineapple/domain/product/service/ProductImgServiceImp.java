package com.fineapple.domain.product.service;

import com.fineapple.Infrastructure.exception.ProductUploadException;
import com.fineapple.domain.product.dto.*;
import com.fineapple.domain.product.repository.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

/**
 * 상품 이미지 업로드 및 수정 관련 기능을 제공하는 서비스 구현체
 * <p>
 * - 상품 등록 시 이미지 파일을 저장하고 DB에 연동 정보를 인서트
 * - 기존 이미지 삭제 및 새 이미지 갱신을 포함한 상품 수정 기능을 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImgServiceImp implements ProductImgService {

    private final ProductMapper productMapper;
    private final MessageSource messageSource;

    @Value("${upload.image-dir}")
    private String imageDir;

    @Value("${upload.image-url-prefix}")
    private String imageUrlPrefix;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }

    /**
     * 상품 이미지 업로드
     *
     * @param productDto 상품 테이블
     * @param imageDto   이미지 테이블
     * @param file       파일
     * @return imageUrl  이미지 url
     * @throws ProductUploadException 유효성 검증 실패, 파일 저장 실패, DB 오류 등 발생 시
     */
    @Transactional
    @Override
    public String upload(ProductInsertDto productDto, ProductImageInsertDto imageDto, MultipartFile file) {
        validateInput(productDto, imageDto, file);

        int rowProduct = productMapper.insertProduct(productDto);
        if (rowProduct <= 0) {
            throwUploadException("error.product.insert.failed");
        }
        Long productId = productDto.getProductId();

        if (imageDto.getProductMain() && productMapper.existsMainImage(productId)) {
            throwUploadException("error.product.image.main_exists");
        }

        String originName = file.getOriginalFilename();
        String savedFileName = UUID.randomUUID() + "_" + originName;
        Path filePath = saveFile(file, savedFileName);

        String imageUrl = imageUrlPrefix + "/" + savedFileName;
        imageDto.setProductId(productId);
        imageDto.setImageUrl(imageUrl);

        try {
            int rowImg = productMapper.insertProductImage(imageDto);
            if (rowImg <= 0) {
                deleteFileSilently(filePath);
                throwUploadException("error.product.image.insert_failed");
            }
        } catch (Exception e) {
            deleteFileSilently(filePath);
            log.error("상품 이미지 DB 저장 실패: productId={}, imageUrl={}", productId, imageUrl, e);
            throwUploadException("error.product.image.db_failed");
        }

        log.info("상품 등록 성공: productId={}, imageUrl={}", productId, imageUrl);
        return imageUrl;
    }

    /**
     * 상품 수정
     *
     * @param productId    상품 아이디
     * @param updateDto    상품 수정 내용
     * @param newImageFile 상품 수정 이미지 파일
     * @throws ProductUploadException 상품 존재 여부, 이미지 오류, DB 업데이트 실패 등 발생 시
     */
    @Transactional
    public void updateProduct(Long productId, ProductUpdateDto updateDto, MultipartFile newImageFile) {
        if (!productMapper.existsProductById(productId)) {
            throwUploadException("error.product.not_found", productId);
        }

        validateCategory(updateDto.getCategoryId());

        ProductInsertDto insertDto = convertToInsertDto(updateDto);
        validateProductFields(insertDto);

        updateDto.setProductId(productId);
        int updated = productMapper.updateProduct(updateDto);
        if (updated <= 0) {
            throwUploadException("error.product.update_failed");
        }


        if (newImageFile != null && !newImageFile.isEmpty()) {
            validateFile(newImageFile);


            ProductImageInsertDto oldImage = productMapper.selectMainImage(productId);
            if (oldImage != null) {
                Path oldImagePath = Paths.get(imageDir).resolve(
                        oldImage.getImageUrl().replace(imageUrlPrefix + "/", "")
                );
                deleteFileSilently(oldImagePath);
                productMapper.deleteProductImage(oldImage.getImageId());
            }


            String originName = newImageFile.getOriginalFilename();
            String savedFileName = UUID.randomUUID() + "_" + originName;
            Path filePath = saveFile(newImageFile, savedFileName);

            String imageUrl = imageUrlPrefix + "/" + savedFileName;

            ProductImageInsertDto newImageDto = new ProductImageInsertDto();
            newImageDto.setProductId(productId);
            newImageDto.setProductMain(updateDto.getProductMain() != null ? updateDto.getProductMain() : false);
            newImageDto.setImageUrl(imageUrl);

            try {
                int rowImg = productMapper.insertProductImage(newImageDto);
                if (rowImg <= 0) {
                    deleteFileSilently(filePath);
                    throwUploadException("error.product.image.insert_failed");
                }
            } catch (Exception e) {
                deleteFileSilently(filePath);
                log.error("상품 이미지 수정 실패: productId={}, imageUrl={}", productId, imageUrl, e);
                throwUploadException("error.product.image.db_failed");
            }
        }

        log.info("상품 수정 완료: productId={}", productId);
    }


    /**
     * 상품 옵션 등록
     *
     * @param productId        상품 아이디
     * @param productOptionDto 상품 옵션 입력 정보
     * @throws ProductUploadException 상품 미존재, 옵션 저장 실패 시
     */
    @Transactional
    @Override
    public void insertOption(Long productId, ProductOptionDto productOptionDto) {
        if (!productMapper.existsProductById(productId)) {
            throwUploadException("error.product.option.exists");
        }
        productOptionDto.setProductId(productId);
        int row = productMapper.insertProductOption(productOptionDto);
        if (row <= 0) {
            throw new ProductUploadException(getMessage("error.product.option.insert_failed"));
        }
    }

    /**
     * 입력값 예외체크 파일, 카테고리, 상품입력정보
     *
     * @param productDto
     * @param imageDto
     * @param file
     */
    private void validateInput(ProductInsertDto productDto, ProductImageInsertDto imageDto, MultipartFile file) {
        validateFile(file);
        validateCategory(productDto.getCategoryId());
        validateProductFields(productDto);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throwUploadException("error.product.image.empty");
        }

        assert file != null;
        String originName = file.getOriginalFilename();
        if (originName == null || originName.isBlank() || originName.contains("..")) {
            throwUploadException("error.product.image.invalid_filename");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throwUploadException("error.product.image.invalid_type");
        }

        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throwUploadException("error.product.image.too_large");
        }
    }

    private void validateCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throwUploadException("error.product.category.invalid");
        }

        if (!productMapper.existsCategoryById(categoryId)) {
            throwUploadException("error.product.category.not_found", categoryId);
        }
    }

    private void validateProductFields(ProductInsertDto dto) {
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throwUploadException("error.product.price.invalid");
        }
    }

    /**
     * 파일 저장
     *
     * @param file
     * @param fileName
     * @return filePath
     */
    private Path saveFile(MultipartFile file, String fileName) {
        Path dirPath = Paths.get(imageDir);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            log.error("디렉토리 생성 실패: {}", dirPath, e);
            throwUploadException("error.product.image.dir_failed", e);
        }

        Path filePath = dirPath.resolve(fileName);
        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", filePath, e);
            throwUploadException("error.product.image.save_failed", e);
        }

        return filePath;
    }

    /**
     * 파일 삭제 메서드
     *
     * @param path
     */
    private void deleteFileSilently(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("파일 삭제 실패: {}", path, e);
        }
    }

    /**
     * 예외 간소화
     *
     * @param key  예외
     * @param args 예외
     */
    private void throwUploadException(String key, Object... args) {
        String msg = getMessage(key, args);
        throw new ProductUploadException(msg);
    }

    private void throwUploadException(String key, Exception e, Object... args) {
        String msg = getMessage(key, args);
        throw new ProductUploadException(msg, e);
    }

    /**
     * dto 업데이트 인서트 dto로 변환
     *
     * @param dto 업데이트 입력정보
     * @return ProductInsertDto
     */
    private ProductInsertDto convertToInsertDto(ProductUpdateDto dto) {
        ProductInsertDto insertDto = new ProductInsertDto();

        insertDto.setProductId(dto.getProductId());
        insertDto.setName(dto.getName());
        insertDto.setDescription(dto.getDescription());
        insertDto.setPrice(dto.getPrice());
        insertDto.setCategoryId(dto.getCategoryId());
        insertDto.setIsActive(dto.getIsActive());

        insertDto.setTargetReleaseDate(dto.getTargetReleaseDate());
        insertDto.setActualReleaseDate(dto.getActualReleaseDate());
        insertDto.setSaleStartDate(dto.getSaleStartDate());
        insertDto.setSaleStopDate(dto.getSaleStopDate());
        insertDto.setSaleRestartDate(dto.getSaleRestartDate());
        insertDto.setSaleEndDate(dto.getSaleEndDate());
        insertDto.setSaleStatus(dto.getSaleStatus());

        return insertDto;
    }


}
