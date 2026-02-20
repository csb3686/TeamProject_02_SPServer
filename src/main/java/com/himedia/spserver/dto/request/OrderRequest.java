package com.himedia.spserver.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderRequest {

    private String category;  // 숙소 / 체험 / 교통

    private int pid;          // 상품 ID
    private int opid;      // 옵션 ID(숙소만)

    private int count;        // 수량

    private String userid;    // 회원 userid
    private int mid;       // 회원 mid
    private String mname; //주문자
    private String mphone; //전화번호

    private Timestamp selecteddate;       // 교통,체험 날짜 선택
    private Timestamp checkInDate;        // 숙소 체크인
    private Timestamp checkOutDate;       // 숙소 체크아웃
}
