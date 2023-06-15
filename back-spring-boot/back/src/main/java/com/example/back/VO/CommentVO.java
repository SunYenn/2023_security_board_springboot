package com.example.back.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CommentVO {

    private int board_idx;
    private int idx;
    private String nickname;
    private String contents;
    private Date writedate;
    private int gup;
    private int seq;
    private int step;
    private int status;

}
