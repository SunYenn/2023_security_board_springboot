package com.example.back.Controller;

import com.example.back.Repository.UserMapper;
import com.example.back.Service.AuthProvider;
import com.example.back.Service.PWencryption;
import com.example.back.Service.LoginService;
import com.example.back.VO.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserCtrl {

    private final LoginService loginService;

    private final AuthProvider authProvider;

    @Autowired
    private UserMapper mapper;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserVO> login(@RequestBody UserVO vo) throws Exception {

        // 계정 체크
        UserVO authentication = loginService.loginMember(vo);

        return ResponseEntity.ok()
                .header("accesstoken", authProvider
                        .createToken(
                                authentication.getId(),
                                authentication.getNickname(),
                                "USER"))
                .body(authentication);
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserVO vo, Errors errors) throws NoSuchAlgorithmException {

        // 데이터 유효성 검사에서 문제 발생시
        if (errors.hasErrors()) {
            log.info("errors : {}", errors.getAllErrors());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("잘못된 데이터입니다.");
        } else {
            // 비밀번호 암호화
            vo.setPw(PWencryption.encrypt(vo.getPw()));
            mapper.join(vo);
            return ResponseEntity.ok("Success");
        }

    }

    // 중복 닉네임 체크
    @PostMapping("/checkNickname")
    public boolean checkNickname(@RequestBody UserVO vo){

        UserVO confirm = mapper.NicknameCheck(vo.getNickname());
        return confirm == null;
    }
    
    // 중복 아이디 체크
    @PostMapping("/checkId")
    public boolean checkId(@RequestBody UserVO vo){

        UserVO confirm = mapper.IdCheck(vo.getId());
        return confirm == null;
    }
}
