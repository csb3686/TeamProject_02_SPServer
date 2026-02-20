package com.himedia.spserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor      // 기본 생성자 추가
@AllArgsConstructor     // 모든 필드 생성자
public class ReviewDto {
    private int rid;
    private String title;
    private String content;
    private int point;
    private String userid;
    private Timestamp indate;
    private String image;

}
