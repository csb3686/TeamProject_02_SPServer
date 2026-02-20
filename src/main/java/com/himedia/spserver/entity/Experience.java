package com.himedia.spserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;

    private String cid; //도시ID(법정동코드)
    private String name; //체험명
    private String content; //내용
    private int price1; //소비자가
    private int price2; //정가
    private int viewcount; //조회수
    private int salecount; //구매수
    private String image; //이미지
    private String hashtag;//해시태그
}


