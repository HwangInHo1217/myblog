package ino.myblog.service;


import ino.myblog.domain.user.Role;
import ino.myblog.domain.user.User;
import ino.myblog.domain.user.UserRepository;
import ino.myblog.dto.UserLoginRequest;
import ino.myblog.dto.UserRegisterRequest;
import ino.myblog.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    // 회원가입 처리
    public void register(UserRegisterRequest request) {
        // 이메일/닉네임 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 객체 생성 및 저장
        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    // 로그인 → 검증 후 JWT 토큰 생성
    public String login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // ✅ 토큰 생성 및 반환
        return jwtTokenProvider.createToken(user.getId(), user.getRole().name());
    }
}
