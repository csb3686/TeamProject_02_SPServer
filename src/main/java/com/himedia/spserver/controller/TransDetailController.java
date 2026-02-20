package com.himedia.spserver.controller;

import com.himedia.spserver.dto.mapper.TransDetailMapper;
import com.himedia.spserver.dto.response.ReviewDto;
import com.himedia.spserver.dto.response.TransDetailDto;
import com.himedia.spserver.entity.Review;
import com.himedia.spserver.entity.Timetable;
import com.himedia.spserver.entity.TransDetail;
import com.himedia.spserver.repository.ReviewRepository;
import com.himedia.spserver.repository.TransDetailRepository;
import com.himedia.spserver.service.ReviewService;
import com.himedia.spserver.service.TimetableService;
import com.himedia.spserver.service.TransDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.apache.catalina.manager.StatusTransformer.formatTime;

@RestController
@RequestMapping("/trans")
@RequiredArgsConstructor
public class TransDetailController {

    private final TransDetailService tds;
    private final ReviewService rs;
    private final TimetableService tts;

    @GetMapping("/getTransDetail/{tid}")
    public HashMap<String, Object> getTransDetail(@PathVariable("tid") int tid) {

        HashMap<String, Object> result = new HashMap<>();
        TransDetailDto trans = tds.getTransDetail(tid);
        List<ReviewDto> reviews = rs.getReviewList("tid", tid);

        String starttime = formatTime(trans.getStarttime()); // 숫자 → HH:MM
        String endtime = formatTime(trans.getEndtime());

        result.put("transData", trans);
        result.put("reviewList", reviews);
        result.put("starttime", starttime);
        result.put("endtime", endtime);

        return result;
    }

    private String formatTime(Integer timeValue) {
        if (timeValue == null) return "-";

        int hour = timeValue / 100;
        int minute = timeValue % 100;

        return String.format("%02d:%02d", hour, minute);
    }


    @GetMapping("/getcount")
    public HashMap<String, Object> getcount(
            @RequestParam("tid") int tid,
            @RequestParam("selectedDate") String selectedDate,
            @RequestParam("count") int count
    ) {


        LocalDate startTs = LocalDate.parse(selectedDate);

        HashMap<String, Object> result = tds.getcount(tid, startTs, count);
        return result;
    }

}
