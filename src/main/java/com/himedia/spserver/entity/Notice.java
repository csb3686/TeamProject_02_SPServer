package com.himedia.spserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nid;
    private String title;
    private String content;
    @CreationTimestamp
    private Timestamp indate;
}
