package com.fineapple.Infrastructure.common;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface CommonCodeService {

    List<CommonCode> getAllCommonCodes();

    CommonCode getCommonCode(String name);

    String getCommonCodeName(String code);  //코드 -> 이름 반환
}
