package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TransDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tid;

    @Column(nullable = true) //오류나서 일단 박아놨어요
    private String cid; //도시ID(법정동코드)
    private String category; //교통구분
    private String company; //회사명
    private String name; //교통편 이름
    private int price1; //소비자가
    private int price2; //정가
    private Integer starttime; //출발시간(편성표 ID)ttimeid
    private Integer endtime; //도착시간(편성표 ID)ttimeid
    private int salecount; //구매수
    private int maxcount; //최대인원
    private String start; //출발지
    private String end; //도착지
    private String image; // 이미지
}
