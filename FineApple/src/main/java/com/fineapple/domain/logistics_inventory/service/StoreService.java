package com.fineapple.domain.logistics_inventory.service;

import com.fineapple.domain.logistics_inventory.dto.StoreDto;
import com.fineapple.domain.logistics_inventory.entity.Store;
import com.fineapple.domain.logistics_inventory.repository.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreMapper storeMapper;

    public Store findStoreById(Long stockId) {
        return storeMapper.findStoreById(stockId);
    }

    public List<StoreDto> findAllStore() {
        return storeMapper.findAllStore();
    }
}
