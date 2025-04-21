    package com.fineapple.application.controller;

    import com.fineapple.domain.logistics_inventory.dto.*;
    import com.fineapple.domain.logistics_inventory.entity.Stock;
    import com.fineapple.domain.logistics_inventory.entity.Store;
    import com.fineapple.domain.logistics_inventory.service.StockService;
    import com.fineapple.domain.logistics_inventory.service.StoreService;
    import com.github.pagehelper.PageHelper;
    import com.github.pagehelper.PageInfo;
    import lombok.AllArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.ModelAndView;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.util.List;

    @RestController
    @RequestMapping("/api/v1")
    @AllArgsConstructor
    @Slf4j
    public class StockAdminController {

        private final StockService stockService;
        private final StoreService storeService;

        @GetMapping("/admin/inventory/store") //스토어선택
        public ResponseEntity<List<StoreDto>> ShowAllStore(){

            //전체 스토어 목록을 가져옴
            List<StoreDto> storeList = storeService.findAllStore();

            return ResponseEntity.ok(storeList);
        }

        @GetMapping("/admin/inventory/{storeId}/stock") // 전체 재고 화면
        public ResponseEntity<List<StockDto>> showStoreAllStock(@PathVariable Long storeId) throws Exception {

            //하나의 스토어에 저장된 재고 정보를 가져옴
            List<StockDto> stockDtoList = stockService.getStockFromStoreAllProduct(storeId);
            return ResponseEntity.ok(stockDtoList);
        }

        @PostMapping("/admin/inventory/{storeId}/stock")
        public ResponseEntity<String> registerNewStock(@PathVariable("storeId") Long storeId, @RequestBody Stock stock) {
            try {
                if (stock.getProductId() == null || stock.getQuantity() == null) {
                    return ResponseEntity.badRequest().body("필수 필드 누락: 상품 ID나 수량이 없습니다.");
                }

                stock = stock.toBuilder().storeId(storeId).build();
                stockService.registerStock(stock);
                return ResponseEntity.status(HttpStatus.CREATED).body("재고가 성공적으로 등록되었습니다.");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace(); // 로그로 찍기
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류: " + e.getMessage());
            }
        }

        @GetMapping("/admin/inventory/{storeId}/stock/{productId}") //상세재고 화면(전체재고화면을 통해 접속)
        public ResponseEntity<StockDetailDto> showStockDetailByProductId(@PathVariable Long storeId, @PathVariable Long productId) throws Exception {

            //각각의 재고 상세 정보를 가져옴 (조회)
            StockDetailDto detail = stockService.getStockDetail(storeId, productId);

            return ResponseEntity.ok(detail);
        }

        @PatchMapping("/admin/inventory/{storeId}/stock/{productId}") // 재고수정
        public ResponseEntity<Void> modifyStockToStoreByProductId(@PathVariable Long storeId, @PathVariable Long productId,
                                                                  @RequestBody StockModifyDto modifyDto) {
            //재고 수정 modifyDto 에 데이터 저장
            stockService.modifyStock(storeId, productId, modifyDto);
            return ResponseEntity.ok().build();
        }

        @GetMapping("/admin/stock/history")
        public ResponseEntity<?> searchStockHistory(
                @RequestParam(defaultValue = "1") int pageNum,
                @RequestParam(defaultValue = "10") int pageSize,
                @RequestParam(defaultValue = "") String keyword) {

            //페이지 네이션 설정
            PageHelper.startPage(pageNum, pageSize);
            List<StockChangeHistoryDto> list = stockService.findStockChangeHistories(keyword, (pageNum - 1) * pageSize, pageSize);
            PageInfo<StockChangeHistoryDto> pageInfo = new PageInfo<>(list);
            return ResponseEntity.ok(pageInfo);
        }
    }