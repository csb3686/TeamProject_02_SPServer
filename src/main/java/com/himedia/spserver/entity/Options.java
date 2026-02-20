package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int opid;
    private int hid; //상품ID
    private String category; // 상품 카테고리(숙소, 교통, 체험)
    private String name; //옵션명
    private int price1; //소비자가
    private int price2; //정가


    private Timestamp startdate; //예약기간-시작
    private Timestamp enddate; //예약기간-종료
    private String content; //옵션 설명

    private int salecount; //구매수
    private String image; //이미지

    private int maxcount; // 객실 수



}
