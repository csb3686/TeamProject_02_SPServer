package com.himedia.spserver.dto.response;

import com.himedia.spserver.entity.Review;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

@Data
public class HReviewListDto {

    private int rid;
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

    private String productname; // 상품명

}
