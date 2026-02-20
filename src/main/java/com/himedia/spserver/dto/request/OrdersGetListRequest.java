package com.himedia.spserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersGetListRequest {

    private int oid;
    private int mid; //주문자ID
    private String mname; //주문자
    private String mphone; //전화번호
    private String category; // 카테고리 구분(숙소, 교통, 체험)
    private int pid; // 상품ID
    private boolean packageOrder; //패키지 여부
    private Timestamp indate; //주문일자
    private int count;
    private Timestamp selecteddate; // 출발/사용일자

    //두개추가
    private Timestamp checkInDate; //체크인날짜
    private Timestamp checkOutDate; //체크아웃날짜

//    options 테이블 칼럼 ㅠㅠ
    private int opid;
    private String name;
    private int price1;

    /* 상품 object(숙소, 교통, 체험) */
    private Object product;

    //이거 은정 추가
    private boolean hasReview; // 리뷰 작성 여부




}
