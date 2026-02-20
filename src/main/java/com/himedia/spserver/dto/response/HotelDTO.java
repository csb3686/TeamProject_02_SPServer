package com.himedia.spserver.dto.response;

import lombok.Data;

@Data
public class HotelDTO {
    private int hid;
    private String name;
    private String image;

    private Integer price1; // 소비자가
    private Integer price2; // 정가
}
