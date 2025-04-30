package com.fineapple.common;


import com.fineapple.application.common.CommonCode;
import com.fineapple.application.common.CommonCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CommonCodeServiceTest {

    @Autowired
    private CommonCodeService commonCodeService;
    @Autowired
    private CacheManager cacheManager;


    @Test
    void getAllCommonCodes() {
        List<CommonCode> list1 = commonCodeService.getAllCommonCodes();
        List<CommonCode> list2 = commonCodeService.getAllCommonCodes();
        assertEquals(list1, list2);
    }

    @Test
    void getCommonCode() {
        CommonCode commonCode = commonCodeService.getCommonCode("CUSTOMER");
        assertNotNull(commonCode);
        assertEquals("ME0101", commonCode.getCode());
        assertEquals("CUSTOMER", commonCode.getName());
        assertEquals("고객", commonCode.getDescription());

    }


}