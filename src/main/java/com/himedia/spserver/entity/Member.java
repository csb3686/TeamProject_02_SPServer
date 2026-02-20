package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mid;

    private String userid;
    private String pwd;
    private String name; //이름
    private String zipCode; // 우편번호
    private String address; //주소
    private String addressDetail; // 상세주소
    private String phone; //전화번호
    private LocalDate birth; //생년월일
    private String email; //이메일
    @CreationTimestamp
    private Timestamp indate; //가입일자
    @Enumerated(EnumType.STRING)
    private SnsType snsType = SnsType.NONE;// 소셜 로그인
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // 계정 권한

}
