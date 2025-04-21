package com.fineapple.application.dto;

public record AdminSalesDto(long totalSales, Integer orderCount, Integer visitors, Integer lowStock
        ,int salesGrowthRate, int orderGrowthRate, int visitorGrowthRate) {
}
