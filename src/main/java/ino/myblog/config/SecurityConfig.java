package ino.myblog.config;

import ino.myblog.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // ✅ 비밀번호 암호화 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ 인증 매니저 등록 (로그인 인증 처리에 필요)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // ✅ Spring Security 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ HTTP Basic 인증 비활성화
                .httpBasic().disable()

                // ✅ CSRF 보호 비활성화 (JWT에서는 보통 사용하지 않음)
                .csrf().disable()

                // ✅ 폼 로그인 비활성화
                .formLogin().disable()

                // ✅ 세션 비활성화 (JWT는 세션을 사용하지 않음)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // ✅ 인증되지 않은 요청에 대한 접근 허용
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll() // 회원가입, 로그인은 허용
                .anyRequest().authenticated()                // 나머지는 인증 필요
                .and()

                // ✅ JWT 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
