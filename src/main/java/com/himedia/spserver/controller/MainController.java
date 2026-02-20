package com.himedia.spserver.controller;

import com.himedia.spserver.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService ms;

    @GetMapping("/getHotelList")
    public HashMap<String, Object> getHotelList(){
        HashMap<String, Object> result = ms.getHotelList();
        return result;
    }

    @GetMapping("/getTransList")
    public HashMap<String, Object> getTransList(){
        HashMap<String, Object> result = ms.getTransList();
        return result;
    }

    @GetMapping("/getExperience")
    public HashMap<String, Object> getExperience(){
        HashMap<String, Object> result = ms.getExperList();
        return result;
    }

}
