package com.fineapple.domain.user.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;



@Getter
@Builder // 객체를 생성할 때 가독성이 좋아지고, 불변성을 유지
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private Long userId;
    private String email;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String userRole;


    private Collection<? extends GrantedAuthority> authorities;

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(isActive); // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(isActive); // null-safe 처리
    }

    // 테스트를 위한 임시 setter
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
