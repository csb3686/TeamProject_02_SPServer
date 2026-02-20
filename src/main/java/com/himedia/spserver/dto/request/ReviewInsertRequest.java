package com.himedia.spserver.dto.request;

import lombok.Data;

@Data
public class ReviewInsertRequest {

    private String userid;
    private int oid; //주문아이디
    private String title; //제목
    private String content; //내용
    private int point; //평점
    private String image; //이미지

}
