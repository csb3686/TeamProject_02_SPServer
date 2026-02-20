package com.himedia.spserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {

    private int opid;
    private Timestamp startdate;
    private Timestamp enddate;
    private String name;
    private String content;
    private int price1;
    private int price2;
    private int hid;

}
