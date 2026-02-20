package com.himedia.spserver.controller;

import com.himedia.spserver.dto.response.ReviewDto;
import com.himedia.spserver.entity.Hotel;
import com.himedia.spserver.entity.Options;
import com.himedia.spserver.repository.OptionsRepository;
import com.himedia.spserver.service.HotelService;
import com.himedia.spserver.service.OptionService;
import com.himedia.spserver.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {

    @Autowired
    HotelService hs;

    private final OptionService ops;
    private final ReviewService rs;

    @GetMapping("/getHotelDetail/{hid}")
    public HashMap<String, Object> getHotelDetail(@PathVariable("hid") int hid) {
        HashMap<String, Object> result = new HashMap<>();
        Hotel hotel = hs.getHotelDetail(hid);
        List<ReviewDto> reviews = rs.getReviewList("hid",hid);
        result.put("hotel", hotel);
        result.put("reviewList", reviews);
        return result;
    }

    @GetMapping("/getOptionList/{hid}")
    public HashMap<String, Object> getOptionList(@PathVariable("hid") int hid) {
        HashMap<String, Object> result = new HashMap<>();
        List<Options> optionsList = ops.findAllByHid(hid); // 여러 옵션
        result.put("optionList", optionsList); // 프론트와 key 맞춤
        return result;
    }

    @GetMapping("/getOptionItem/{opid}")
    public HashMap<String, Object> getOptionItem(
            @PathVariable("opid") int opid
    ) {
        HashMap<String, Object> result = new HashMap<>();
        Options option = ops.findByOpid(opid);
        Hotel hotel = hs.getHotelDetail(option.getHid());
        result.put("optionDetail", option);
        result.put("hotelDetail", hotel);
        return result;
    }

    @GetMapping("/countRoom")
    public HashMap<String, Object> getRoomCount(@RequestParam("opid") int opid, @RequestParam("startdate") OffsetDateTime startdate, @RequestParam("enddate")OffsetDateTime enddate, @RequestParam("count") int count) {

        System.out.println("startdate:"+startdate);
        System.out.println("enddate:"+enddate);

        Timestamp startTs = Timestamp.from(startdate.toInstant());
        Timestamp endTs = Timestamp.from(enddate.toInstant());

        HashMap<String, Object> result = hs.countRoom(opid, startTs, endTs ,count);
        return result;
    }

}
