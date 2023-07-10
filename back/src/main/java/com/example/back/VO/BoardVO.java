package com.example.back.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class BoardVO {

    private int rnum;
    private int idx;
    private String nickname;
    private String title;
    private String contents;
    private String orifilename;
    private String realfilename;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date writedate;
    private int views;
    private String pw;

    private ArrayList<CommentVO> commentList = new ArrayList<CommentVO>();

}
