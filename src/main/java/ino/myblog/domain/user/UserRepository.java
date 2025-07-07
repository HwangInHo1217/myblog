package ino.myblog.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 사용자 엔티티에 대한 DB 접근을 처리하는 JPA Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 닉네임 중복 확인용
    boolean existsByNickname(String nickname);

    // 이메일 중복 확인용
    boolean existsByEmail(String email);
}