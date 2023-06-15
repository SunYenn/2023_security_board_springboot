package com.example.back.Service;

import com.example.back.Exception.UserNotFoundException;
import com.example.back.Repository.UserMapper;
import com.example.back.VO.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoadAccount implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        UserVO entity = userMapper.NicknameCheck(nickname);

        if (entity == null)
            throw new UserNotFoundException("존재하지 않는 회원의 정보입니다.");

        UserDetails member = new CustomUserDetails(entity.getId(), nickname, "USER");

        return member;
    }
}