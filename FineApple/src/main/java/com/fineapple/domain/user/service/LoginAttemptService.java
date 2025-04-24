package com.fineapple.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class LoginAttemptService {

    // 사용자별 시도 정보 저장 (username -> 로그인 시도 수 & 잠금 시간)
    // ConcurrentHashMap: HashMap과 비슷하지만 여러 스레드가 동시에 접근해도 안전
    private final ConcurrentHashMap<String, Integer> attemptsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LocalDateTime> lockTimeMap = new ConcurrentHashMap<>();

    private final int MAX_ATTEMPTS = 5;
    private final int LOCK_MINUTES = 5;

    /**
     * 로그인 실패 시 호출되는 메서드
     */
    public void loginFailed(String username) {
        // 이미 잠금 상태라면 그냥 리턴
        if (isBlocked(username)) return;

        int attempts = attemptsMap.getOrDefault(username, 0) + 1;
        attemptsMap.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockTimeMap.put(username, LocalDateTime.now());
            log.warn("사용자 {} 계정이 잠겼습니다.", username);
        }
    }

    /**
     * 계정이 잠겨있는지 확인
     */
    public boolean isBlocked(String username) {
        if (!lockTimeMap.containsKey(username)) return false;

        LocalDateTime lockTime = lockTimeMap.get(username);
        // 언제 잠겼는지 확인하고, 지금 시간이 얼마나 지났는지 확인
        long minutesPassed = Duration.between(lockTime, LocalDateTime.now()).toMinutes();

        // 잠금 시간 지났으면 해제
        if (minutesPassed >= LOCK_MINUTES) {
            lockTimeMap.remove(username);
            attemptsMap.put(username, 0); // 시도 횟수도 초기화
            return false;
        }

        return true;
    }

    /**
     * 남은 시도 횟수 리턴
     */
    public int getRemainingAttempts(String username) {
        return MAX_ATTEMPTS - attemptsMap.getOrDefault(username, 0);
    }

    /**
     * 남은 잠금 시간 리턴
     */
    public String getRemainingLockTime(String username) {
        if (!isBlocked(username)) return "0분";

        LocalDateTime lockTime = lockTimeMap.get(username);
        long minutesPassed = Duration.between(lockTime, LocalDateTime.now()).toMinutes();

        long remaining = LOCK_MINUTES - minutesPassed;
        return remaining + "분";
    }

    public String getLockMessage(String username) {
        if (isBlocked(username)) {
            return "계정이 잠겼습니다. " + getRemainingLockTime(username) + " 후에 다시 시도하세요.";
        }
        return null;
    }
}
