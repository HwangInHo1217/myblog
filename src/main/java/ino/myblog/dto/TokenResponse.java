package ino.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 로그인 성공 시 반환할 JWT 응답 객체
@Getter
@AllArgsConstructor
public class TokenResponse {
    private String token;
}
