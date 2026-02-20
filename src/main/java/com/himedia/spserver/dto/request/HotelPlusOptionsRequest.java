package com.himedia.spserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.html.Option;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HotelPlusOptionsRequest {

    private int hid;
    private String name;
    private String content;
    private String notice;
    private String image;
    private String x;
    private String y;
    private String cid;

    OptionRequest options; // 옵션 리스트

}

