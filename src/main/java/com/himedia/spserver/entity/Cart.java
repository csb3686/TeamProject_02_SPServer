package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartid;

    private String cid; // 도시 아이디
    private String category; // 카테고리 구분(숙소, 교통, 체험)
    private Integer pid; //상품 ID
    private Integer opid; //옵션 ID
    private String name; //상품명
    private String opname; //옵션명
    private Integer price; //가격
    private Integer count; //개수 (호텔에서는 박수)
    private Timestamp selecteddate; //선택날짜
    private Integer mid; // member-id
    private String userid; // member-userid
    private String image; // 상품 이미지

    private Timestamp checkInDate; //체크인날짜
    private Timestamp checkOutDate; //체크아웃날짜
    @Column(columnDefinition = "INT default 0")
    private int ispackage;
}
