package ino.myblog.controller;

import ino.myblog.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPIController {

    // ✅ 인증된 사용자 정보 확인용 API
    @GetMapping("/me")
    public User getMyInfo(@AuthenticationPrincipal User user) {
        // Spring Security가 JWT에서 인증된 유저를 자동으로 주입
        return user; // 테스트를 위해 엔티티 그대로 반환 (실제론 DTO로 바꾸는게 좋음)
    }
}
