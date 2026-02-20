package com.himedia.spserver.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExList {

    private int eid;
    private String cid;
    private String name;
    private String content;
    private int price1;
    private int price2;
    private Timestamp selectedDate;
    private String image;
    private int count;
}
