package com.himedia.spserver.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CartInsertRequest {

    //requset-> 프론트가 입력해야하는값만 들어가야함.
    //서버가 DB를 보고 채워야 하는 값은 절대 넣지 않음.

    private String category;  // 숙소 / 체험 / 교통

    private int pid;          // 상품 ID
    private int opid;      // 옵션 ID(숙소만)

    private int count;        // 수량

    private String userid;    // 회원 userid
    private int mid;       // 회원 mid

    private Timestamp selecteddate;       // 교통,체험 날짜 선택
    private Timestamp checkInDate;        // 숙소 체크인
    private Timestamp checkOutDate;       // 숙소 체크아웃

}

