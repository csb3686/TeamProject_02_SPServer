package com.himedia.spserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdminNoticeDto {
    private int nid;
    private String title;
    private String content;
    private Timestamp indate;
}
