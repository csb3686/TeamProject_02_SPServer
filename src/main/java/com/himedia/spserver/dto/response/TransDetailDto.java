package com.himedia.spserver.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TransDetailDto {
    private int tid;
    private String category;
    private String company;
    private String name;
    private int price1;
    private int price2;
    private String start;
    private String end;
    private int starttime;
    private int endtime;
    private int maxcount; //최대인원
    private String image; // 이미지
    private List<ReviewDto> reviews;
}
