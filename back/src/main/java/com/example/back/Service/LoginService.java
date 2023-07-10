package com.example.back.Service;

import com.example.back.Exception.ForbiddenException;
import com.example.back.Exception.UserNotFoundException;
import com.example.back.Repository.UserMapper;
import com.example.back.VO.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Service("signService")
@RequiredArgsConstructor
public class LoginService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private ModelMapper modelMapper;

    public UserVO loginMember(UserVO vo) throws NoSuchAlgorithmException {

        // 회원 엔티티 객체 생성 및 조회시작
        UserVO confirm = mapper.IdCheck(vo.getId());

        // 계정 유무 체크
        if (confirm == null)
            throw new UserNotFoundException("This account does not exist");

        // 비밀번호 일치 체크
        if (!PWencryption.encrypt(vo.getPw()).equals(confirm.getPw()))
            throw new ForbiddenException("Passwords do not match");

        // 회원정보를 인증클래스 객체(authentication)로 매핑
        return modelMapper.map(confirm, UserVO.class);
    }

}
