package com.example.back.VO;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PagingVO {

    private ArrayList<BoardVO> boardList = new ArrayList<BoardVO>();

    private int page_size = 10;
    private int total_count = 0;
    private int total_page = 0;
    private int current_page = 1;
    private int start_no = 0;
    private int start_page = 0;

    private String search_obj;
    private String search_category;

    public PagingVO() {}

    public PagingVO(int page_size, int total_count, int current_page) {
        this.page_size = page_size;
        this.total_count = total_count;
        this.current_page = current_page;
        calculator();
    }

    private void calculator() {
        total_page = (total_count - 1) / page_size + 1;
        current_page = current_page > total_page ? total_page : current_page;
        start_no = (current_page - 1) * page_size;
        start_page = (current_page - 1) / page_size * page_size + 1;
    }

}
