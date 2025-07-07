package ino.myblog.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    // 고유 ID (Auto Increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일 (회원가입 시 ID 역할, 중복 불가)
    @Column(nullable = false, unique = true)
    private String email;

    // 암호화된 비밀번호
    @Column(nullable = false)
    private String password;

    // 닉네임 (중복 허용 안 함)
    @Column(nullable = false, unique = true)
    private String nickname;

    // 권한 (기본값: USER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    // 생성자 (Builder 패턴)
    @Builder
    public User(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    // 닉네임 수정
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    // ✅ 권한 목록 반환 (1개만 반환하는 구조)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    // ✅ 로그인에 사용될 사용자 식별자 (username)
    @Override
    public String getUsername() {
        return email;
    }

    // ✅ 계정이 만료되지 않았는지 여부 (true면 사용 가능)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ✅ 계정이 잠겨 있지 않은지 여부 (true면 사용 가능)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ✅ 자격 증명이 만료되지 않았는지 여부 (true면 사용 가능)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ✅ 계정이 활성화되어 있는지 여부 (true면 사용 가능)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
