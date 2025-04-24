package com.fineapple.domain.logistics_inventory.service;

import com.fineapple.Infrastructure.common.CommonCodeService;
import com.fineapple.domain.logistics_inventory.dto.*;
import com.fineapple.domain.logistics_inventory.entity.Stock;
import com.fineapple.domain.logistics_inventory.entity.StockChangeHistory;
import com.fineapple.domain.logistics_inventory.entity.Store;
import com.fineapple.domain.logistics_inventory.repository.StockMapper;
import com.fineapple.domain.logistics_inventory.repository.StoreMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockMapper stockMapper;
    private final StoreMapper storeMapper;

    //전체 스토어 목록 조회
    public List<StoreDto> showAllStore() {
        return storeMapper.findAllStore();
    }

    //하나의 스토어에 저장된 재고 가져옴
    public List<StockDto> getStockFromStoreAllProduct(@Param("storeId") Long storeId) {
        return stockMapper.getStockFromStoreAllProduct(storeId);
    }

    public StockDetailDto getStockDetail(@Param("storeId") Long storeId, @Param("productId") Long productId) {
        //각각의 상품에 대한 상세 재고 쿼리문 실행
        return stockMapper.getStockFromStoreByProductId(storeId, productId);
    }

    public StockDetailDto getExistingStock(@Param("storeId") Long storeId, @Param("productId") Long productId) {
        StockDetailDto existingStock = stockMapper.findStockByStoreIdAndProductId(storeId, productId);
        if(existingStock == null) {
            return null;
        }else{
            return existingStock;
        }
    }

    @Transactional
    public void registerStock(Stock stock) {
        // 1. 기존 재고 조회
        StockDetailDto existingStock = getExistingStock(stock.getStoreId(), stock.getProductId());
        if (existingStock != null) {
            throw new IllegalStateException("이미 등록된 재고상품입니다.");
        }
        String status = stock.getStockStatus();
        if (status == null || status.isBlank()) {
            status = "ST0301"; // 기본 상태 코드: 정상
        }
        // 새 재고 등록 로직 (기존 그대로)
        LocalDateTime now = LocalDateTime.now();
        Stock newStock = Stock.builder()
                .storeId(stock.getStoreId())
                .productId(stock.getProductId())
                .quantity(stock.getQuantity())
                .createdAt(now)
                .updatedAt(now)
                .stockStatus(status)
                .lastRestockDate(null)
                .minStockLevel(stock.getMinStockLevel())
                .maxStockLevel(stock.getMaxStockLevel())
                .firstStockInDate(now)
                .safetyStockLevel(stock.getSafetyStockLevel())
                .stockInQuantity(stock.getQuantity())
                .stockOutQuantity(0)
                .isRestockRequired(false)
                .build();

        stockMapper.uploadNewStock(newStock);

        Store store = storeMapper.findStoreById(stock.getStoreId());
        StockChangeHistory history = StockChangeHistory.builder()
                .stockId(newStock.getStockId())
                .productId(newStock.getProductId())
                .storeId(newStock.getStoreId())
                .storeName(store.getName())
                .stockInQuantity(newStock.getQuantity())
                .stockInReason("ST0401")
                .stockOutQuantity(0)
                .previousStock(0)
                .newStock(newStock.getQuantity())
                .changedAt(now)
                .build();

        stockMapper.insertStockChangeHistory(history);
    }

    @Transactional
    public void modifyStock(Long storeId, Long productId, StockModifyDto dto) {
        // 기존 재고 정보 조회
        StockDetailDto origin = stockMapper.getStockFromStoreByProductId(storeId, productId);
        if (origin == null) {
            throw new RuntimeException("해당 재고 정보가 존재하지 않습니다.");
        }

        // 변경 요청과 기존 값을 병합합니다.
        Integer updatedQuantity = dto.getQuantity() != null ? dto.getQuantity() : origin.getQuantity();
        LocalDateTime updatedLastRestockDate = dto.getLastRestockDate() != null ? dto.getLastRestockDate() : origin.getLastRestockDate();
        LocalDateTime updatedFirstStockInDate = dto.getFirstStockInDate() != null ? dto.getFirstStockInDate() : origin.getFirstStockInDate();
        LocalDateTime updatedLastStockOutDate = dto.getLastStockOutDate() != null ? dto.getLastStockOutDate() : origin.getLastStockOutDate();
        Integer updatedMinStockLevel = dto.getMinStockLevel() != null ? dto.getMinStockLevel() : origin.getMinStockLevel();
        Integer updatedMaxStockLevel = dto.getMaxStockLevel() != null ? dto.getMaxStockLevel() : origin.getMaxStockLevel();
        Integer updatedSafetyStockLevel = dto.getSafetyStockLevel() != null ? dto.getSafetyStockLevel() : origin.getSafetyStockLevel();
        Integer updatedStockInQuantity = dto.getStockInQuantity() != null ? dto.getStockInQuantity() : origin.getStockInQuantity();
        Integer updatedStockOutQuantity = dto.getStockOutQuantity() != null ? dto.getStockOutQuantity() : origin.getStockOutQuantity();
        Boolean updatedIsRestockRequired = dto.getIsRestockRequired() != null ? dto.getIsRestockRequired() : origin.getIsRestockRequired();

        // (옵션) 최소/최대 검증
        if (updatedMinStockLevel != null && updatedQuantity < updatedMinStockLevel) {
            throw new IllegalArgumentException("입력된 수량이 최소 재고량보다 작습니다.");
        }
        if (updatedMaxStockLevel != null && updatedQuantity > updatedMaxStockLevel) {
            throw new IllegalArgumentException("입력된 수량이 최대 재고량보다 큽니다.");
        }

        // 재고 상태 결정 (예시: 0이면 품절, 그 외 정상)
        String updatedStockStatus = updatedQuantity == 0 ? "ST0302" : "ST0301";

        // 업데이트할 값들을 Map에 담습니다.
        Map<String, Object> params = new HashMap<>();
        params.put("stockId", origin.getStockId());
        params.put("quantity", updatedQuantity);
        params.put("updatedAt", LocalDateTime.now());
        params.put("lastRestockDate", updatedLastRestockDate);
        params.put("stockStatus", updatedStockStatus);
        params.put("firstStockInDate", updatedFirstStockInDate);
        params.put("lastStockOutDate", updatedLastStockOutDate);
        params.put("minStockLevel", updatedMinStockLevel);
        params.put("maxStockLevel", updatedMaxStockLevel);
        params.put("safetyStockLevel", updatedSafetyStockLevel);
        params.put("stockInQuantity", updatedStockInQuantity);
        params.put("stockOutQuantity", updatedStockOutQuantity);
        params.put("isRestockRequired", updatedIsRestockRequired);

        // 업데이트 실행
        stockMapper.updateStock(params);

        // 변경 이력 기록
        Store store = storeMapper.findStoreById(origin.getStoreId());
        StockChangeHistory history = StockChangeHistory.builder()
                .stockId(origin.getStockId())
                .productId(origin.getProductId())
                .storeId(origin.getStoreId())
                .storeName(store.getName())
                .stockInQuantity(updatedStockInQuantity)
                .stockInReason("MD0902")
                .stockOutQuantity(updatedStockOutQuantity)
                .stockOutReason("MD0902")
                .previousStock(origin.getQuantity())
                .newStock(updatedQuantity)
                .changedAt(LocalDateTime.now())
                .build();
        stockMapper.insertStockChangeHistory(history);
    }
    /**
     * TODAY 재고부족 총량e
     */
    public Integer countStockQuantityByPeriod(LocalDate startDate, LocalDate endDate) {
        return stockMapper.selectStockCountQuantityToday(startDate, endDate);
    }

    public List<StockChangeHistoryDto> getAllStockChangeHistory() {
        return stockMapper.selectAllStockChangeHistory();
    }

    public List<StockChangeHistoryDto> findStockChangeHistories(String keyword, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return stockMapper.findStockChangeHistories(keyword, offset, pageSize);
    }
}
