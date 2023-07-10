package com.example.back.Repository;

import com.example.back.VO.BoardVO;
import com.example.back.VO.CommentVO;
import com.example.back.VO.PagingVO;
import com.example.back.VO.UserVO;

import java.util.ArrayList;

@org.apache.ibatis.annotations.Mapper
public interface UserMapper {
    
    // 회원가입
    void join(UserVO vo);

    // 닉네임 중복 체크
    UserVO NicknameCheck(String nickname);

    // 아이디 중복 체크
    UserVO IdCheck(String id);

}
