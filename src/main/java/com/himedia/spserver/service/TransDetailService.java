package com.himedia.spserver.service;

import com.himedia.spserver.dto.mapper.TransDetailMapper;
import com.himedia.spserver.dto.response.TransDetailDto;
import com.himedia.spserver.entity.TransDetail;
import com.himedia.spserver.repository.OrdersRepository;
import com.himedia.spserver.repository.TransDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransDetailService {

    private final TransDetailRepository tdr;
    private final TransDetailMapper tdm;
    private final OrdersRepository or;

    public TransDetailDto getTransDetail(int tid) {
        TransDetail transData = tdr.findByTid(tid);


        return tdm.toTranDetailDto(transData);

    }

    public HashMap<String, Object> getcount(int tid, LocalDate selectedDate, int count) {
        HashMap<String, Object> result = new HashMap<>();
        TransDetail trans = tdr.findByTid(tid); // 총좌석 가져오기
        Integer realcount = or.countTrans(tid, selectedDate); //주문되어있는는 개수

        int remainingSeats = trans.getMaxcount() - realcount; // 남은 좌석

        System.out.println("remainingSeats"+ remainingSeats);

        if(trans.getMaxcount() - (realcount + count) < 0){
            result.put("msg", "해당 날짜의 좌석 개수가 부족합니다. 다른 날짜를 선택해주세요.");
        }else{
            result.put("msg", "ok");
        }

        // 남은 좌석 값 추가
        result.put("remainingSeats", remainingSeats);


        return result;
    }

}
