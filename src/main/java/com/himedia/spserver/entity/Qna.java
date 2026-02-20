package com.himedia.spserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Data
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qid;
    private String title;
    private String content;
    private Timestamp indate;
    private String answer;
}
