package com.example.back.Repository;

import com.example.back.VO.BoardVO;
import com.example.back.VO.CommentVO;
import com.example.back.VO.PagingVO;

import java.util.ArrayList;

@org.apache.ibatis.annotations.Mapper
public interface BoardMapper {

    // 글 가져오기
    int countBoard(PagingVO vo);
    ArrayList<BoardVO> selectBoardList(PagingVO vo);

    // 글 한건 가져오기
    BoardVO selectBoardByIdx(int idx);
    void incrementViews(int idx); // 조회수 증가
    ArrayList<CommentVO> selectCommentByIdx(int idx); // 댓글 가져오기

    // 글 등록
    void insertBoard(BoardVO vo);
    int countBoardByOrifilename(String orifilename); // 중복 파일 체크

    // 글 삭제
    void deleteBoardByIdx(int idx);
    void deleteCommentByIdx(int idx); // 댓글 함께 삭제

    // 글 수정
    void updateBoard(BoardVO vo);

    // 댓글 입력
    void insertComment(CommentVO vo);

    // 댓글 수정
    void updateComment(CommentVO vo);

    // 댓글 상태를 삭제
    void deleteComment(int idx);

    // 댓글 상태를 복구
    void restoreComment(int idx);

    // 대댓글 그룹 설정을 위한 호출
    int getGup(int boardIdx);

    // 대댓글 시퀀스 설정
    void setSeqs(CommentVO vo);

}
