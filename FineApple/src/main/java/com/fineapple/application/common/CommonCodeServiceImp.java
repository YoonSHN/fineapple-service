package com.fineapple.application.common;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 공통 코드(Common Code) 조회 기능을 제공하는 서비스 구현체
 *
 * - 상태 코드, 분류 코드 등 시스템 전반에 걸쳐 반복적으로 사용되는 상태코드 정보를 제공
 * - 해당 코드는 스프링 부트가 실행시 자동으로 db에서 조회애서 캐시에 저장후 사용
 */
@Service
public class CommonCodeServiceImp implements CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;

    public CommonCodeServiceImp(CommonCodeRepository commonCodeRepository) {
        this.commonCodeRepository = commonCodeRepository;
    }

    /**
     * 모든 상태 코드를 불러와 캐시에 저장후 사용
     * @return List<CommonCode>
     */
    @Cacheable("commonCodes")
    @Override
    public List<CommonCode> getAllCommonCodes() {
        return commonCodeRepository.findAll();
    }


    @Cacheable(value = "commonCodes", key = "#name")
    @Override
    public CommonCode getCommonCode(String name) {
        return commonCodeRepository.findByCodeName(name);
    }

    @Cacheable(value="commonCodes" , key="#code")
    @Override
    public String getCommonCodeName(String code){
        return commonCodeRepository.getCommonCodeName(code);
    }
}
