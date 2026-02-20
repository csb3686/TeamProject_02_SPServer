package com.himedia.spserver.service;

import com.himedia.spserver.entity.Hotel;
import com.himedia.spserver.entity.Options;
import com.himedia.spserver.repository.HotelRepository;
import com.himedia.spserver.repository.OptionsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
public class HotelService {

    @Autowired
    HotelRepository hr;

    @Autowired
    OptionsRepository or;

    public Hotel getHotelDetail(int hid) {

        Optional<Hotel> result = hr.findById(hid);
        if(result.isPresent()) {
            return result.get();
        }else {
            return null;
        }
    }

    public HashMap<String, Object> countRoom(int opid, Timestamp startdate, Timestamp enddate, int count) {

        HashMap<String, Object> result = new HashMap<>();

        //해당기간에 예약된 객실수
        Integer realcount = or.countRoom(opid, startdate, enddate);

        System.out.println("realcount:"+realcount);
        Options options = or.findByOpid(opid);
        int maxRooms = options.getMaxcount();
        int remainRooms = maxRooms - realcount;

        if(count > remainRooms) {
            result.put("msg", "해당기간에 객실수가 부족합니다.");
        }else{
            result.put("msg", "ok");
        }
        return result;
    }
}
