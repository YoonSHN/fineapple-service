package com.fineapple.application.service;

import com.fineapple.application.dto.TimeSeriesDto;
import com.fineapple.domain.logistics_inventory.repository.ShipmentAnalyticsMapper;
import com.fineapple.domain.logistics_inventory.repository.StockAnalyticsMapper;
import com.fineapple.domain.order_payment.repository.CouponAnalyticsMapper;
import com.fineapple.domain.order_payment.repository.OrderAnalyticsMapper;
import com.fineapple.domain.order_payment.repository.PaymentAnalyticsMapper;
import com.fineapple.domain.order_payment.repository.RefundAnalyticsMapper;
import com.fineapple.domain.support.repository.InquiryAnalyticsMapper;
import com.fineapple.domain.user.repository.UserAnalyticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {
    private final ShipmentAnalyticsMapper shipmentAnalyticsMapper;
    private final OrderAnalyticsMapper orderAnalyticsMapper;
    private final PaymentAnalyticsMapper paymentAnalyticsMapper;
    private final RefundAnalyticsMapper refundAnalyticsMapper;
    private final StockAnalyticsMapper stockAnalyticsMapper;
    private final UserAnalyticsMapper userAnalyticsMapper;
    private final InquiryAnalyticsMapper inquiryAnalyticsMapper;
    private final CouponAnalyticsMapper couponAnalyticsMapper;


    public List<TimeSeriesDto> getDailyOrderCount() {
        return orderAnalyticsMapper.getDailyOrderCount();
    }
    public List<TimeSeriesDto> getProductSalesCount() {
        return orderAnalyticsMapper.getProductSalesCount();
    }
    public List<TimeSeriesDto> getDailyRevenue() {
        return orderAnalyticsMapper.getDailyRevenue();
    }
    public List<TimeSeriesDto> getMonthlyRevenue() {return orderAnalyticsMapper.getMonthlyRevenue();}
    public List<TimeSeriesDto> getPredictedDailyRevenueFromDb() { return orderAnalyticsMapper.getPredictedDailyRevenue();}
    public List<TimeSeriesDto> getPredictedMonthlyRevenueFromDb() { return orderAnalyticsMapper.getMonthlyPredictedValues();}
    public List<TimeSeriesDto> getDailyPaidAmount() {
        return paymentAnalyticsMapper.getDailyPaidAmount();
    }
    public List<TimeSeriesDto> getDailyPaidByMethod() {
        return paymentAnalyticsMapper.getDailyPaidByMethod();
    }
    public List<TimeSeriesDto> getDailyRefundAmount() {
        return refundAnalyticsMapper.getDailyRefundAmount();
    }
    public List<TimeSeriesDto> getRefundFailCount() {
        return refundAnalyticsMapper.getRefundFailCount();
    }
    public List<TimeSeriesDto> getDailyStockIn() {
        return stockAnalyticsMapper.getDailyStockIn();
    }
    public List<TimeSeriesDto> getDailyStockOut() {
        return stockAnalyticsMapper.getDailyStockOut();
    }
    public List<TimeSeriesDto> getDailyNetStockChange() {
        return stockAnalyticsMapper.getDailyNetStockChange();
    }
    public List<TimeSeriesDto> getDailyLoginCount() {
        return userAnalyticsMapper.getDailyLoginCount();
    }
    public List<TimeSeriesDto> getDailySignupCount() {
        return userAnalyticsMapper.getDailySignupCount();
    }
    public List<TimeSeriesDto> getDailyInquiryCount() {
        return inquiryAnalyticsMapper.getDailyInquiryCount();
    }
    public List<TimeSeriesDto> getDailyCouponUseCount() {
        return couponAnalyticsMapper.getDailyCouponCount();
    }

    public List<TimeSeriesDto> getDailyCouponCountByCoupon() {
        return couponAnalyticsMapper.getDailyCouponCountByCoupon();
    }

    public List<TimeSeriesDto> getDailyDispatchCount() {
        return shipmentAnalyticsMapper.getDailyDispatchCount();
    }
    public List<TimeSeriesDto> getDailyDelayCount() {
        return shipmentAnalyticsMapper.getDailyDelayCount();
    }
}
