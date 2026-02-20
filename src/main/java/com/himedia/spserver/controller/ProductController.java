package com.himedia.spserver.controller;

import com.himedia.spserver.dto.response.HReviewListDto;
import com.himedia.spserver.dto.response.ReviewDto;
import com.himedia.spserver.entity.Experience;
import com.himedia.spserver.entity.Options;
import com.himedia.spserver.entity.Review;
import com.himedia.spserver.service.ProductService;
import com.himedia.spserver.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService ps;

    @Autowired
    ReviewService rs;


    @GetMapping("/getExperienceList")
    public HashMap<String, Object> getExperienceList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword
    ) {
        return ps.getExperienceList(page, sort, keyword);
    }

    @GetMapping("/getCityList")
    public HashMap<String, Object> getCityList(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("CityList", ps.getCityList());
        return result;
    }

    @GetMapping("/getHotelList")
    public HashMap<String, Object> getHotelList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword
    ) {
        return ps.getHotelList(page, sort, keyword);
    }

    @GetMapping("/getTransList")
    public HashMap<String, Object> getTransList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword
    ) {
        return ps.getTransList(page, sort, keyword);
    }

    @GetMapping("/getEReviewList")
    public Map<String, Object> getEReviewList(
            @RequestParam String category,
            @RequestParam int page
    ) {
        Page<HReviewListDto> reviewPage = ps.getEReviewListByCategory(category, page);


        Map<String, Object> result = new HashMap<>();
        result.put("list", reviewPage.getContent());
        result.put("totalPages", reviewPage.getTotalPages());
        result.put("totalElements", reviewPage.getTotalElements());
        return result;
    }

    @GetMapping("/getBestEReviewList")
    public HashMap<String, Object> getBestEReviewList(@RequestParam(required = false) String category) {
        HashMap<String, Object> result = new HashMap<>();

        if (category != null) {
            result.put("bestEReviewList", ps.getBestEReviewListByCategory(category));
        } else {
            result.put("bestEReviewList", ps.getBestEReviewList());
        }
        return result;
    }

    @GetMapping("/getHReviewList")
    public Map<String, Object> getHReviewList(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<HReviewListDto> reviewPage = ps.getHReviewListByCategory(category, page);


        Map<String, Object> result = new HashMap<>();
        result.put("list", reviewPage.getContent());
        result.put("totalPages", reviewPage.getTotalPages());
        result.put("totalElements", reviewPage.getTotalElements());
        return result;
    }



    @GetMapping("/getBestHReviewList")
    public HashMap<String, Object> getBestHReviewList(@RequestParam(required = false) String category) {
        HashMap<String, Object> result = new HashMap<>();

        if (category != null) {
            result.put("bestHReviewList", ps.getBestHReviewListByCategory(category));
        } else {
            result.put("bestHReviewList", ps.getBestHReviewList());
        }
        return result;
    }


    @GetMapping("/getTReviewList")
    public HashMap<String, Object> getTReviewList(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        HashMap<String, Object> result = new HashMap<>();
        Page<HReviewListDto> reviewPage;

        reviewPage = ps.getTReviewListByCategoryPage(category, page, size);

        result.put("tReviewList", reviewPage.getContent());
        result.put("totalCount", reviewPage.getTotalElements());
        result.put("totalPages", reviewPage.getTotalPages());

        return result;
    }




    @GetMapping("/getBestTReviewList")
    public HashMap<String, Object> getBestTReviewList(@RequestParam(required = false) String category) {
        HashMap<String, Object> result = new HashMap<>();

        if (category != null) {
            result.put("bestTReviewList", ps.getBestTReviewListByCategory(category));
        } else {
            result.put("bestTReviewList", ps.getBestTReviewList());
        }
        return result;
    }





    @GetMapping("/experienceDetail/{eid}")
    public HashMap<String, Object> experienceDetail(@PathVariable("eid") int eid) {
        HashMap<String, Object> result = new HashMap<>();
        Experience experience = ps.experienceDetail(eid);
        List<ReviewDto> reviews = rs.getReviewList("eid",eid);
        result.put("experience", experience);
        result.put("reviewList", reviews);
        return result;
    }

    @GetMapping("/getOptionList/{eid}")
    public HashMap<String, Object> getOptionList(@PathVariable("eid") int eid) {
        HashMap<String, Object> result = new HashMap<>();
        List<Options> optionsList = ps.findAllByEid(eid); // 여러 옵션
        result.put("optionList", optionsList); // 프론트와 key 맞춤

        return result;
    }


    @GetMapping("/TopCityProducts")
    public HashMap<String, Object> TopCityProducts() {
        return ps.getTopCityProducts();
    }


    //조회수
    @PostMapping("/increaseHotelView/{hid}")
    public HashMap<String, Object> increaseHotelView(@PathVariable("hid") int hid){
        HashMap<String, Object> result = new HashMap<>();
        ps.increaseHotelView(hid);
        return result;
    }

//    @PostMapping("/increaseTransView/{tid}")
//    public HashMap<String, Object> increaseTransView(@PathVariable("tid") int tid){
//        HashMap<String, Object> result = new HashMap<>();
//        ps.increaseTransView(tid);
//        return result;
//    }

    @PostMapping("/increaseExView/{eid}")
    public HashMap<String, Object> increaseExView(@PathVariable("eid") int eid){
        HashMap<String, Object> result = new HashMap<>();
        ps.increaseExView(eid);
        return result;
    }


}
