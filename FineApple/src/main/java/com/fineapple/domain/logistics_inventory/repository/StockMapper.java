package com.fineapple.domain.logistics_inventory.repository;

import com.fineapple.domain.logistics_inventory.dto.StockChangeHistoryDto;
import com.fineapple.domain.logistics_inventory.dto.StockDetailDto;
import com.fineapple.domain.logistics_inventory.dto.StockDto;
import com.fineapple.domain.logistics_inventory.entity.Stock;
import com.fineapple.domain.logistics_inventory.entity.StockChangeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;



import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface StockMapper {

    //  상품에 대한 재고
    Integer selectStockByProductId(Long productId);

    Integer selectStockCountQuantityToday(LocalDate startDate, LocalDate endDate);

    // 특정 스토어에서 전체 상품 및 재고 목록 조회 (전체 재고 화면)
    List<StockDto> getStockFromStoreAllProduct(@Param("storeId") Long storeId);

    // 특정 스토어에서 상품 재고 상세 조회 (상세 재고 화면)
    StockDetailDto getStockFromStoreByProductId(@Param("storeId") Long storeId, @Param("productId")Long productId);

    // 재고 등록 + 재고내역 자동 등록(재고 등록후)
    void uploadNewStock(Stock stock);

    // 입출고 내역 저장
    void insertStockChangeHistory(StockChangeHistory stockChangeHistory);

    void updateStock(Map<String, Object> params);

    List<StockChangeHistoryDto> selectAllStockChangeHistory();


    //  이력 목록 조회 (추가 필요)
    List<StockChangeHistoryDto> findStockChangeHistories(@Param("keyword") String keyword,
                                                         @Param("offset") int offset,
                                                         @Param("limit") int limit);
    //  총 개수 조회
    int countStockChangeHistories(@Param("keyword") String keyword);

    // db에 저장된 stock 가져오기

    StockDetailDto findStockByStoreIdAndProductId(@Param("storeId") Long storeId, @Param("productId") Long productId);


}