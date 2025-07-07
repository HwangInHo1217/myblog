package ino.myblog.controller;


import ino.myblog.dto.TokenResponse;
import ino.myblog.dto.UserLoginRequest;
import ino.myblog.dto.UserRegisterRequest;
import ino.myblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthAPIController {
    private final UserService userService;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인 → JWT 발급
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request); // 로그인 성공 시 토큰 생성
        return ResponseEntity.ok(new TokenResponse(token)); // 응답 바디에 토큰 포함
    }
}
