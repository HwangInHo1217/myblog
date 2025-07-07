package ino.myblog.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    // JWT 비밀키 (application.yml에서 주입)
    @Value("${jwt.secret}")
    private String secretKey;

    // JWT 유효 시간 (밀리초) - 예: 1시간
    private final long tokenValidTime = 60 * 60 * 1000L;

    private Key key;

    // 비밀키를 기반으로 Key 객체 생성
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * ✅ 토큰 생성
     * @param userId 사용자 ID
     * @param role   권한 (ex. USER, ADMIN)
     */
    public String createToken(Long userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 사용자 식별자
                .claim("role", role) // 권한 클레임 추가
                .setIssuedAt(now)    // 발급 시간
                .setExpiration(expiry) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * ✅ 토큰에서 사용자 ID 추출
     */
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * ✅ 토큰에서 Role(권한) 추출
     */
    public String getUserRole(String token) {
        Claims claims = parseClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * ✅ 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims 문자열이 비어있습니다.");
        }
        return false;
    }

    /**
     * ✅ 토큰 파싱 (예외처리 포함)
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰의 클레임 반환
        }
    }
}