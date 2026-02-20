package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oid;

    private String cid; // 도시 ID
    private int mid; //주문자ID
    @Column(columnDefinition = "INT default 0" )
    private int opid; // 옵션아이디
    private String mname; //주문자
    private String mphone; //전화번호
    private String category; // 카테고리 구분(숙소, 교통, 체험)
    private int pid; // 상품ID
    @Column(columnDefinition = "INT default 0")
    private int packageOrder; //패키지 여부
    @CreationTimestamp
    private Timestamp indate; //주문일자
    private int price; //가격
    private int count; //개수
    private Timestamp selecteddate; //선택 날짜(숙소, 체험, 교통)

    private Timestamp checkInDate; //체크인날짜
    private Timestamp checkOutDate; //체크아웃날짜

    @ManyToOne
    @JoinColumn(name = "member_mid")
    private Member member;

}
