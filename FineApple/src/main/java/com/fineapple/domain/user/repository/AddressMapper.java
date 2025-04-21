package com.fineapple.domain.user.repository;

import com.fineapple.domain.user.entity.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper {
    Address findById(Long deliveryId);
}