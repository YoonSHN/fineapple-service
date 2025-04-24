package com.fineapple.application.controller.view;

import com.fineapple.domain.support.dto.InquiryRequestDto;
import com.fineapple.domain.user.entity.User;
import com.fineapple.domain.support.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportInquiryViewController {

    // 1. 문의 작성 페이지
    @GetMapping("/inquiry/new")
    public String showInquiryForm(Model model) {
        model.addAttribute("inquiryRequestDto", new InquiryRequestDto());
//        RestTemplate restTemplate = new RestTemplate();
        return "support/inquiry-form";
    }

    // 2. 전체 문의 목록 페이지
    @GetMapping("/all-inquiries")
    public String showAllInquiries(Model model) {
        model.addAttribute("isMyPage", false);
        return "support/inquiry-list";
    }

    // 3. 내 문의 목록 페이지
    @GetMapping("/my/inquiries")
    public String showMyInquiries(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("isMyPage", true);
        model.addAttribute("userId", user.getUserId());
        return "support/inquiry-list";
    }

    // 4. 문의 상세 페이지
    @GetMapping("/inquiry/{inquiryId}")
    public String showInquiryDetail(
            @PathVariable Long inquiryId,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("inquiryId", inquiryId);
        model.addAttribute("userId", user != null ? user.getUserId() : null);
        return "support/inquiry-detail";
    }
}
