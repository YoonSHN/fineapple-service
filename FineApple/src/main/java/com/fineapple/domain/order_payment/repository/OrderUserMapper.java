package com.fineapple.domain.order_payment.repository;

import com.fineapple.domain.order_payment.dto.OrderItemDetailUserDto;
import com.fineapple.domain.order_payment.dto.OrderUserDto;
import com.fineapple.domain.order_payment.dto.OrderUserInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;



@Mapper
public interface OrderUserMapper {

    List<OrderUserDto> selectRecentOrdersById(Map<String, Long> params);

    OrderUserDto selectOrderItemDetailByOrderCode(Long orderCode);

    OrderUserInfoDto getUserInfo(Long userId);
    void insertOrderAndItems(OrderUserDto orderDTO);
    void insertOrderItemDetails(@Param("orderId") Long orderId, @Param("orderItems") List<OrderItemDetailUserDto> orderItems);

    boolean existsByOrderCode(@Param("orderCode") Long orderCode);

    void updateOrderStatus(@Param("orderId") Long orderId, @Param("status") String status);

    default Long getPaymentIdByOrderId(@Param("orderId") Long orderId) {
        return null;
    }
    void insertOrderStatus(@Param("orderId") Long orderId,
                           @Param("orderStatus") String orderStatus,
                           @Param("paymentStatus") String paymentStatus);


    List<OrderItemDetailUserDto> findOrderItemByOrderId(Long orderId);
    String getProductNameById(Long productId);

    OrderUserDto selectOrderById(Long orderId);

    String getCommonName(String code);

}
