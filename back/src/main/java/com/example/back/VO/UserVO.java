package com.example.back.VO;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserVO {

    @NotBlank(message = "아이디는 필수 입력 사항입니다.")
    private String id;

    @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[~!@#$%^&+=])(?=\\S+$).{6,}$",
            message = "비밀번호는 6자리 이상, 숫자 1개, 영문자 1개, 특수문자 1개 이상을 포함해야 합니다.")
    private String pw;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "이메일의 형식을 갖추어야 합니다.")
    private String email;

}
