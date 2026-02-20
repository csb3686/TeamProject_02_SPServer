package com.himedia.spserver.dto.response;

import com.himedia.spserver.entity.*;
import lombok.Data;

@Data
public class OrderDetailResponse {
    private Orders order;

    // 숙소
    private Hotel hotel;
    private Options option;

    // 체험
    private Experience experience;

    // 교통
    private TransDetail trans;

    //시간표
    private String starttime;   // 출발 시간표
    private String endtime;     // 도착 시간표
}
