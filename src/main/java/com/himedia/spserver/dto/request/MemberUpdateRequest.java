package com.himedia.spserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberUpdateRequest {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userid; // 아이디
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String pwd; // 비밀번호
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name; //이름
    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    private String zipCode; //우편번호
    @NotBlank(message = "주소는 필수 입력값입니다.")
    private String address; //주소
    private String addressDetail; //상세주소
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String phone; //전화번호
    private LocalDate birth; //생년월일
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "올바르지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email; // 이메일
}
