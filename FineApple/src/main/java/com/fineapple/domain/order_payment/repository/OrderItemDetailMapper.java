package com.fineapple.domain.order_payment.repository;

import com.fineapple.domain.order_payment.entity.OrderItemDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface OrderItemDetailMapper {

    void insertOrderItemDetailsList(@Param("orderItemDetailList") List<OrderItemDetail> orderItemDetailList);
}
