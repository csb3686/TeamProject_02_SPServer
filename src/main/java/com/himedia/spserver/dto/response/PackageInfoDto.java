package com.himedia.spserver.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.himedia.spserver.dto.mapper.EmptyListDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageInfoDto {

    private String cid;
    private int opid;
    private Timestamp startdate;
    private Timestamp enddate;
    private int sttid;
    private Timestamp sttransselecteddate;
    private int sttranscount;
    private int entid;
    private Timestamp entransselecteddate;
    private int entranscount;
    private int hid;
    private int hotelnights;
    private int hotelcount;

    private String userid;

}
