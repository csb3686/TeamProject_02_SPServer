package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hid;

    private String cid; // 도시 ID (법정동코드)
    private String name; // 숙소명
    private String x; // X좌표
    private String y; // Y좌표
    private String content; // 설명
    private String notice; // 유의사항
    private String spotcontent; // 위치안내
    private int viewcount; // 조회수
    private int salecount; // 구매수
    private String image; // 이미지

    @Transient  // ✅ DB에는 없는 필드
    private Integer price1; // 소비자가

    @Transient
    private Integer price2; // 정가

}
