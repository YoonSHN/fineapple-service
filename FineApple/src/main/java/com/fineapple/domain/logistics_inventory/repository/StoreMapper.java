package com.fineapple.domain.logistics_inventory.repository;

import com.fineapple.domain.logistics_inventory.dto.StoreDto;
import com.fineapple.domain.logistics_inventory.entity.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StoreMapper {

    @Select("SELECT store_id, name FROM Store WHERE store_id = #{storeId}")
    Store findStoreById(Long storeId);

    @Select("SELECT store_id, name, location, store_number FROM Store ")
    List<StoreDto> findAllStore();
}
