package com.fineapple.application.common;

import com.fineapple.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Random;


// 해당 컴포넌트에서는 장바구니 접근전 유저의 로그인 정보를 바탕으로
//로그인한 유저의 장바구니 또는 비회원 유저의 장바구니로 접근하는 로직 담당

@Component
public class CartUserIdProvider {

    public Long resolveUserId(User user, HttpServletRequest request) {
        //유저의 로그인 정보가 있다면 로그인 유저의 정보 반환
        if (user != null) return user.getUserId();

        //비회원
            //기존의 장바구니 생성 정보가 있다면 해당정보 반환
        Object guestId = request.getSession().getAttribute("userId");
        if (guestId != null) return (Long) guestId;

            //기존 정보가 없으면 새로 생성
            //현재 시간 + 랜덤 숫자 조합으로 유니크 Long 생성
        Long newGuestId = System.currentTimeMillis() + new Random().nextInt(1000);
        request.getSession().setAttribute("userId", newGuestId);
        return newGuestId;
    }


}
