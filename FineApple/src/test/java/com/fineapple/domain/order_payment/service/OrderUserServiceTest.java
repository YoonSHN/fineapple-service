//package com.fineapple.domain.order_payment.service;
//
//import com.fineapple.domain.logistics_inventory.entity.Stock;
//import com.fineapple.domain.logistics_inventory.repository.StockMapper;
//import com.fineapple.domain.order_payment.dto.OrderItemDetailUserDto;
//import com.fineapple.domain.order_payment.dto.OrderUserDto;
//import com.fineapple.domain.order_payment.entity.Orders;
//import com.fineapple.domain.order_payment.repository.OrderCartMapper;
//import com.fineapple.domain.order_payment.repository.OrderStockMapper;
//import com.fineapple.domain.order_payment.repository.OrderUserMapper;
//import com.fineapple.domain.product.dto.ProductDetailDto;
//import com.fineapple.domain.product.dto.ProductInsertDto;
//import com.fineapple.domain.product.entity.Product;
//import com.fineapple.domain.product.repository.ProductMapper;
//import com.fineapple.domain.product.service.ProductService;
//import com.fineapple.domain.user.dto.CartItemDto;
//import com.fineapple.domain.user.repository.CartMapper;
//import com.github.pagehelper.PageInfo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@Transactional
//public class OrderUserServiceTest {
//
//    @Autowired
//    private OrderUserService orderUserService;
//
//    @Autowired
//    private CartMapper cartMapper;
//
//    @Autowired
//    private OrderUserMapper orderUserMapper;
//
//    @Autowired
//    private OrderStockMapper orderStockMapper;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//
//    @Test
//    @DisplayName("selectOrderItemDetailByOrderCode() 주문 상세 조회 테스트")
////    public void testSelectOrderItemDetailByOrderCode() {
////
////        Long insertedCode = 2024042400008L;
////        List<OrderUserDto> orderList = orderUserService.selectOrderItemDetailByOrderCode(insertedCode);
////
////        assertFalse(orderList.isEmpty(), "조회된 주문 리스트는 비어 있지 않아야 합니다.");
////
////        OrderUserDto orderUserDto = orderList.get(0); // 첫 번째 항목 기준으로 검증
////        assertEquals(insertedCode, orderUserDto.getOrderCode());
////        assertEquals(new BigDecimal("2478308.00"), orderUserDto.getTotalPrice());
////        assertEquals("Apple Watch Series 10", orderUserDto.getItemName());
////        assertEquals(new BigDecimal("1983841.00"), orderUserDto.getItemPrice());
////        assertEquals("010-4444-5555", orderUserDto.getUserPhone());
////    }
//
//    @Test
//    @DisplayName("회원/비회원의 주문 목록 조회 테스트")
//    public void testSelectRecentOrdersById() {
//
//        Long insertedId = 3L;
//        PageInfo<OrderUserDto> orderList = orderUserService.selectRecentOrdersById(insertedId, 1, 10);
//
//        assertNotNull(orderList);
//        assertFalse(orderList.getList().isEmpty());
//
//        OrderUserDto lastOrder = orderList.getList().getFirst();
//        assertEquals(new BigDecimal("197475.00"), lastOrder.getTotalPrice());
//        assertEquals(new BigDecimal("47835.00"), lastOrder.getDiscountPrice());
//        assertEquals("정여진", lastOrder.getUserName());
//    }
//
//    // 주문 생성 테스트
//    @Test
//    @DisplayName("회원/비회원 주문 생성 테스트")
//    public void testCreateOrder() {
//
//        Long userId = 6L;
//        Long guestId = 1L;
//        Long cartId = 4L;
//
//        // 2. 장바구니에 담을 상품 추가 dd
//        List<OrderItemDetailUserDto> orderItems = new ArrayList<>();
//        OrderItemDetailUserDto item = new OrderItemDetailUserDto();
//
//        Long productId = 3L;
//        String productName = orderUserMapper.getProductNameById(productId);
//
//        if (productName != null) {
//            item.setProductId(productId);  // 예시 상품 ID
//            item.setItemName(productName);  // 상품 이름을 가져와 설정
//        } else {
//            // 상품이 존재하지 않는 경우에 대한 처리
//            item.setItemName("24 일반 노트북");  // 기본 이름 또는 예외 처리
//        }
//
//        item.setOptionId(1L);  // 예시 옵션 ID
//        item.setItemQuantity(1);  // 주문 수량
//        item.setItemPrice(new BigDecimal("2000000"));  // 상품 가격
//
//        orderItems.add(item);
//
//        List<CartItemDto> cartItems = cartMapper.findCartItems(cartId);
//        assertFalse(cartItems.isEmpty(), "장바구니가 비어 있으면 안 됩니다.");
//
//        // 장바구니에 상품 추가
//        for (OrderItemDetailUserDto orderItem : orderItems) {
//            CartItemDto cartItem = new CartItemDto();
//            cartItem.setCartId(cartId);
//            cartItem.setProductId(orderItem.getProductId());
//            cartItem.setProductName(orderItem.getItemName());
//            cartItem.setOptionsJson(orderItem.getItemAdditional());
//            cartItem.setQuantity(Long.valueOf(orderItem.getItemQuantity()));
//            cartItem.setProductPrice(orderItem.getItemPrice());
//
//            // CartMapper의 insertCartItem() 메서드를 사용해 장바구니에 상품을 추가
//            cartMapper.insertCartItem(cartItem);
//        }
//
//        // 3. 장바구니에서 비회원 장바구니에 상품이 추가되었는지 확인
//        List<CartItemDto> updatedCartItems = cartMapper.findCartItems(cartId);
//        assertFalse(updatedCartItems.isEmpty(), "장바구니가 비어 있으면 안 됩니다.");
//
//        // 4. 주문 정보 생성
//        OrderUserDto orderUserDto = new OrderUserDto();
//        orderUserDto.setGuestId(guestId);  // 비회원의 경우 Guest ID 설정
//        orderUserDto.setCartId(cartId);  // 장바구니 ID 설정
//        orderUserDto.setOrderItems(orderItems);
//        orderUserDto.setItemName("24 일반 노트북");
//        orderUserDto.setTotalPrice(new BigDecimal("2000000"));  // 총 결제 금액 설정
//        orderUserDto.setDiscountPrice(new BigDecimal("0"));  // 할인 금액 설정
//        orderUserDto.setPaymentMethod("OR0501");  // 결제 방식 설정 (예: 카드 결제)
//
//        // 5. 실제 주문 생성 서비스 호출
//        Long orderCode = orderUserService.createOrder(orderUserDto);
//
//        // 6. 주문 코드 검증 (정상적으로 주문이 생성되었는지 확인)
//        assertNotNull(orderCode);  // 생성된 주문 코드가 null이 아니어야 함
//        assertTrue(orderCode > 0);  // 유효한 주문 코드 값이 생성되었는지 확인
//
//    }
//    // 재고 조회 테스트
//    @Test
//    @DisplayName("재고 조회 테스트")
//    public void testSelectStockByProductId() {
//        Long productId = 3L;
//
//        int stock = orderStockMapper.selectStockByProductId(productId);
//
//        assertNotNull(stock, "재고가 null이 아니어야 합니다.");
//        assertEquals(20, stock);
//    }
//
//
//}
