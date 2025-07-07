package ino.myblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 로그인 요청 시 사용하는 DTO
@Getter
@Setter
public class UserLoginRequest {

    @Email(message = "유효한 이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
