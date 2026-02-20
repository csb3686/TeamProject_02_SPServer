package com.himedia.spserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberVerifyCodeIdRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name; // 이름
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "올바르지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email; // 이메일
    @NotNull(message = "인증번호는 필수 입력값입니다.")
    private int inputCode; // 입력받은 인증번호

}
