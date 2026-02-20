package com.himedia.spserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.himedia.spserver.dto.request.CartGetListRequest;
import com.himedia.spserver.dto.response.ExList;
import com.himedia.spserver.dto.response.HotelDTO;
import com.himedia.spserver.dto.response.OptionDTO;
import com.himedia.spserver.dto.response.PackageInfoDto;
import com.himedia.spserver.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/package")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService ps;

    @GetMapping("/getCidByKeyword")
    public String getCid(@RequestParam("keyword") String keyword){

        String cityOne = ps.getCidByPlace(keyword);

        return cityOne;
    }


    @GetMapping("/getTransList")
    public HashMap<String, Object> getTransList(@RequestParam("place") String place, @RequestParam("start") String start){

        String[] parts = place.split("\\s+");

        String city = parts.length > 0 ? parts[0] : "";
        String country = parts.length > 1 ? parts[1] : "";
        System.out.println("city:"+city);

        HashMap<String, Object> result = ps.getTransListByPlace(city, start);

        return result;
    }

    @GetMapping("/getHotelListByCid")
    public HashMap<String, Object> getHotelListByCid(@RequestParam("cid") String cid){
        HashMap<String, Object> result = ps.getHotelListByCid(cid);
        return result;
    }

    @GetMapping("/getOptionListByHid")
    public HashMap<String, Object> getOptionListByHid(@RequestParam("hid") int hid){
        HashMap<String , Object>result = ps.getOptionListByHid(hid);
        return result;
    }

    @GetMapping("/getExListByCid")
    public HashMap<String, Object> getExListByCid(@RequestParam("cid") String cid){
        HashMap<String , Object> result = ps.getExListByCid(cid);
        return result;
    }

    @PostMapping("/insertCartPackage")
    public HashMap<String, Object> insertCartPackage(@RequestBody PackageInfoDto pdto) throws JsonProcessingException {

        System.out.println("pdto" + pdto);
        HashMap<String, Object> result = ps.insertCartPackage(pdto);
        return result;
    }

    @PostMapping("/insertCartExlist")
    public void insertCartExlist(@RequestBody ExList exlist, @RequestParam("packageid") int packageid, @RequestParam("userid") String  userid)   {
        ps.insertCartExlist(exlist, packageid, userid);
    }

    @GetMapping("/getCartList")
    public ResponseEntity<List<CartGetListRequest>> getCartList(@RequestParam("packageid") int packageid){
        List<CartGetListRequest> cartList = ps.getCartList(packageid);
        return ResponseEntity.ok(cartList);
    }

    @GetMapping("/getHotelByHid")
    public HotelDTO getHotelByHid(@RequestParam("hid") int hid){
        HotelDTO hotel = ps.getHotelByHid(hid);
        return hotel;
    }

    @GetMapping("/getOptionByOpid")
    public OptionDTO getOptionByOpid(@RequestParam("opid") int opid){
        OptionDTO op = ps.getOptionByOpid(opid);
        return op;
    }
}
