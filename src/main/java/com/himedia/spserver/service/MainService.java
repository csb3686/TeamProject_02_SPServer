package com.himedia.spserver.service;

import com.himedia.spserver.entity.*;
import com.himedia.spserver.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {

    private final HotelRepository hr;
    private final OptionsRepository or;
    private final TransDetailRepository tdr;
    private final MainReposirory mr;
    private final CityRepository cr;

    public HashMap<String, Object> getHotelList() {
        HashMap<String, Object> result = new HashMap<>();
        List<Hotel> hotelList = hr.findTop4ByOrderByViewcountDesc();
        result.put("hotelList", hotelList);

        List<Integer> hotelPrice = new ArrayList<>();
        for (Hotel hotel : hotelList){
            Options opt = or.findTop1ByHidOrderByPrice1Asc(hotel.getHid());
        hotelPrice.add(opt.getPrice1());
        }
        result.put("price", hotelPrice);
        return result;
    }

    public HashMap<String, Object> getTransList() {
        HashMap<String, Object> result = new HashMap<>();
        List<TransDetail> tdl = tdr.findTop4ByOrderBySalecountDesc();
        result.put("transList", tdl);
        return result;
    }

    public HashMap<String, Object> getExperList() {
        HashMap<String, Object> result = new HashMap<>();
        List<Experience> ex = mr.findTop4ByOrderBySalecountDesc();
        result.put("experList", ex);

        List<City> experCity = new ArrayList<>();
        for (Experience exp : ex){
            List<City> city = cr.findByCidStartingWith(String.valueOf(exp.getCid()));
            if(!city.isEmpty()) {
                experCity.add(city.get(0));
            }else{
                experCity.add(null);
            }
        }
        result.put("experCity", experCity);

        return result;
    }
}
