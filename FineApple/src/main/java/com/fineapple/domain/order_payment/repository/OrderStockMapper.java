package com.fineapple.domain.order_payment.repository;

import com.fineapple.domain.logistics_inventory.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderStockMapper {

    void decreaseStock(Long productId, Integer quantity);

    int selectStockByProductId(Long productId);
}
