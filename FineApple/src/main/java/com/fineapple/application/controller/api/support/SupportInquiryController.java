package com.fineapple.application.controller.api.support;

import com.fineapple.domain.support.dto.InquiryRequestDto;
import com.fineapple.domain.support.dto.InquiryResponseDetailDto;
import com.fineapple.domain.support.dto.InquiryResponseDto;
import com.fineapple.domain.support.service.InquiryService;
import com.fineapple.domain.user.entity.User;
import com.fineapple.domain.user.service.UserDetailServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
public class SupportInquiryController {
    private final InquiryService inquiryService;
    private final UserDetailServiceImp userDetailService;

    // 문의 전체 조회
    @GetMapping("/all-inquiries")
    public ResponseEntity<List<InquiryResponseDto>> findAll() {
        return ResponseEntity.ok(inquiryService.getAllInquiries());
    }

    // 내 문의 목록
    @GetMapping("/my-inquiries")
    public ResponseEntity<List<InquiryResponseDto>> myInquiry(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long userId = user.getUserId();
        List<InquiryResponseDto> inquiries = inquiryService.getMyInquiries(userId);
        return ResponseEntity.ok(inquiries);
    }

    // 글 등록
    @PostMapping("/write")
// @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Map<String, String>> createInquiry(@RequestBody InquiryRequestDto dto,
                                                             @AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        Long userId = user.getUserId();
        String name = userDetailService.getUserRealNameById(userId);

        dto.setEmail(user.getEmail());
        dto.setName(name);

        inquiryService.createInquiry(dto, userId);

        // JSON 응답으로 반환
        Map<String, String> response = Map.of("message", "문의글이 등록되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 글 상세 조회
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDetailDto> myInquiry(
            @PathVariable Long inquiryId,
            @AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        InquiryResponseDetailDto dto = inquiryService.getMyDetailInquiry(user.getUserId(), inquiryId);
        return ResponseEntity.ok(dto);
    }

    // 상태에 따른 필터
    @GetMapping("/filter")
    public ResponseEntity<List<InquiryResponseDto>> filterInquiries(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String status) {
        List<InquiryResponseDto> filteredInquiries = inquiryService.filterInquiries(searchTerm, status);
        return ResponseEntity.ok(filteredInquiries);
    }



}