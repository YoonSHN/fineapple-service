package com.fineapple.domain.support.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryRequestDto {
    private Long inquiryId; // 문의 고유번호
    private String subject; // 문의 제목
    private String content; // 문의 내용
    private Long userInfo; // 회원정보 id
    private String inquiryStatus; // 고객지원 상태코드
    private String inquiryType;
    private boolean acceptPrivacyPolicy;
    private String email;
    private String name; // 실명

//    private LocalDateTime createdAt; // 문의 등록일
//    private LocalDateTime updatedAt; // 문의 수정일

    /*IN0101,PENDING,일반 대기 상태
    IN0102,PROGRESS,진행 중 상태
    IN0103,RESOLVED,해결 완료 상태*/


    /*IN0201,ORDER,주문 관련 문의
    IN0202,DELIVERY,배송 관련 문의
    IN0203,REFUND,환불 관련 문의
    IN0204,ETC,기타 관련 문의*/

//    private String assignedTo; // 문의 현재담당자
//    private LocalDateTime responseDueDate; // 응답 마감기한

    /*IN0301,LOW,문의 낮은 우선순위
    IN0302,MEDIUM,문의 중간 우선순위
    IN0303,HIGH,문의 높은 우선순위*/
//    private String priorityCode;
//    private Long orderId; // 주문Id
//    private Long orderItemDetailId; // 주문상품상세Id

}
