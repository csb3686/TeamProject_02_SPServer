package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"oid"}) // 주문당 리뷰 1개
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rid;

    @Column(nullable = false)
    private int oid; //오더ID


    private int hid; //숙소ID
    private int tid; //교통ID
    private int eid; //체험ID
    private int mid; //작성자ID
    private String title; //제목
    private String content; //내용
    private int point; //평점
    private Timestamp indate; //작성일자
    private String image; //이미지
    private String category;
    private String userid;

}
