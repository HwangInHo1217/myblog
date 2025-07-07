package ino.myblog.domain.user;

import org.springframework.security.core.GrantedAuthority;

// 사용자 권한 정의
public enum Role implements GrantedAuthority {
    USER,   // 일반 사용자
    ADMIN;  // 관리자

    @Override
    public String getAuthority() {
        return name();
    }
}
