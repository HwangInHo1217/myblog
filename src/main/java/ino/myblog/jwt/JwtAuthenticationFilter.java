package ino.myblog.jwt;


import ino.myblog.domain.user.User;
import ino.myblog.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 매 요청마다 실행되는 JWT 인증 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청 헤더에서 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰이 있고, 유효한 경우에만 인증 처리
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserId(token);

            // DB에서 사용자 정보 조회
            User user = userRepository.findById(userId).orElse(null);

            if (user != null) {
                // 인증 객체 생성 (비밀번호는 null)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // 요청 정보로부터 상세 정보 설정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 인증 객체를 SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    /**
     * ✅ Authorization 헤더에서 토큰 추출
     * 예: Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Bearer 접두어 제거
            return bearerToken.substring(7);
        }
        return null;
    }
}
