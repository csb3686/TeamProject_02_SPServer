package com.himedia.spserver.controller;

import com.himedia.spserver.dto.request.AdminNoticeEditRequest;
import com.himedia.spserver.dto.request.AdminNoticeWriteRequest;
import com.himedia.spserver.dto.response.AdminMemberDto;
import com.himedia.spserver.dto.response.HotelDTO;
import com.himedia.spserver.entity.Hotel;
import com.himedia.spserver.repository.EProduct;
import com.himedia.spserver.entity.Options;
import com.himedia.spserver.service.AdminService;
import com.himedia.spserver.util.Paging;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService as;

    @GetMapping("/getMemberList")
    public ResponseEntity<Map<String, Object>> getMemberList(@RequestParam("page") int page) {
        Page<AdminMemberDto> memberPage = as.getMemberList(page);

        // Page 정보 활용해서 paging 계산
        Paging paging = new Paging();
        paging.setPage(page);            // 현재 페이지
        paging.setDisplayPage(7);        // 7개씩 표시
        paging.setDisplayRow(7);         // 한 페이지 7개
        paging.setTotalCount((int) memberPage.getTotalElements());
        paging.calPaging();              // 기존 로직 그대로 사용

        // response 구성
        Map<String, Object> map = new HashMap<>();
        map.put("paging", paging);
        map.put("memberList", memberPage.getContent());

        return ResponseEntity.ok(map);
    }

    @GetMapping("/getNoticeList/{page}")
    public HashMap<String, Object> getNoticeList(@PathVariable("page") int page) {
        return as.getNoticeList(page);
    }

    @PostMapping("/writeNotice")
    public HashMap<String, Object> writeNotice(@RequestBody @Valid AdminNoticeWriteRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        as.writeNotice(request);
        result.put("result", "success");
        return result;
    }

    @PostMapping("/editNotice")
    public HashMap<String, Object> editNotice(@RequestBody @Valid AdminNoticeEditRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        as.updateNotice(request);
        result.put("result", "success");
        return result;
    }

    @DeleteMapping("/deleteNotice/{nid}")
    public HashMap<String, Object> deleteNotice(@PathVariable int nid) {
        HashMap<String, Object> result = new HashMap<>();
        as.deleteNotice(nid);
        result.put("result", "success");
        return result;
    }

    @GetMapping("/getOneMember")
    public HashMap<String, Object> getOneMember(@RequestParam("mid") int mid) {
        HashMap<String, Object> result = as.getOneMember(mid);
        return result;
    }

    @GetMapping("/getEProduct/{page}")
    public HashMap<String, Object> getEProduct(
            @PathVariable("page") int page,
            @RequestParam(required = false) String search
    ) {
        return as.getEProduct(page, search);
    }


    @GetMapping("/AdminEProduct/{eid}")
    public HashMap<String, Object> getAdminEProduct(@PathVariable("eid") int eid) {
        return as.getAdminEProduct(eid);
    }


    @PostMapping("/updateAdminEProduct")
    public HashMap<String, Object> updateAdminEProduct(
            @RequestParam int eid,
            @RequestParam String name,
            @RequestParam String content,
            @RequestParam String city,
            @RequestParam int price1,
            @RequestParam int price2,
            @RequestParam int viewcount,
            @RequestParam int salecount,
            @RequestParam String hashtag,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        HashMap<String, Object> result = new HashMap<>();
        boolean updateCheck = as.updateAdminEProduct(
                eid, name, content, city, price1, price2, viewcount, salecount, hashtag, imageFile
        );
        result.put("msg", updateCheck ? "ok" : "fail");
        return result;
    }

    @PostMapping(value = "/insertAdminEProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HashMap<String, Object> insertAdminEProduct(
            @RequestParam String name,
            @RequestParam String content,
            @RequestParam String city,
            @RequestParam int price1,
            @RequestParam int price2,
            @RequestParam int viewcount,
            @RequestParam int salecount,
            @RequestParam String hashtag,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        HashMap<String, Object> result = new HashMap<>();

        boolean ok = as.insertAdminEProduct(
                name, content, city, price1, price2,
                viewcount, salecount, hashtag, imageFile
        );

        result.put("msg", ok ? "ok" : "fail");
        return result;
    }

    @GetMapping("/searchCity")
    public HashMap<String, Object> searchCity(@RequestParam("keyword") String keyword) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cities", as.searchCity(keyword));
        return result;
    }





    @GetMapping("/getHProduct/{page}")
    public HashMap<String, Object> getHProduct(
            @PathVariable("page") int page,
            @RequestParam(required = false) String search
        ) {


            HashMap<String, Object> result = as.getHProduct(page, search);


            List<Hotel> hotelList = (List<Hotel>) result.get("hProduct");


            List<HotelDTO> hotelDTOList = as.convertToDTO(hotelList);

            result.put("hProduct", hotelDTOList);

            return result;

        }


    @GetMapping("/AdminHProduct/{hid}")
    public HashMap<String, Object> getAdminHProduct(@PathVariable("hid") int hid) {
        return as.getAdminHProduct(hid);
    }


    @PostMapping("/updateAdminHProduct")
    public HashMap<String, Object> updateAdminHProduct(
            @RequestParam int hid,
            @RequestParam String name,
            @RequestParam String content,
            @RequestParam String cid,
            @RequestParam int viewcount,
            @RequestParam int salecount,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        HashMap<String, Object> result = new HashMap<>();
        boolean updateCheck = as.updateAdminHProduct(hid, name, content, cid, viewcount, salecount, imageFile);
        result.put("msg", updateCheck ? "ok" : "fail");
        return result;
    }





    @PostMapping("/insertHProduct")
    public HashMap<String, Object> insertHProduct(
            @RequestParam String name,
            @RequestParam String content,
            @RequestParam String notice,
            @RequestParam String cid,
            @RequestParam int viewcount,
            @RequestParam int salecount,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
            ) throws IOException {
        HashMap<String, Object> result = new HashMap<>();
        result = as.insertHProduct(name, content, notice, cid, viewcount, salecount, imageFile);
        return result;
    }


    @PostMapping("/insertAdminHOption")
    public HashMap<String, Object> insertAdminHOption(
            @RequestBody Options options,
            @RequestParam("hid") int hid
    ) {
        HashMap<String, Object> result = new HashMap<>();
        result = as.insertAdminHOption(options, hid);
        return result;
    }

    // ✅ 새로 추가: 옵션 이미지 업로드
    @PostMapping("/insertHOptionImage")
    public HashMap<String, Object> insertHOptionImage(
            @RequestParam("opimage") MultipartFile imageFile,
            @RequestParam("opid") int opid
    ) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            as.updateOptionImage(opid, imageFile);
            result.put("message", "이미지 업로드 성공");
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "이미지 업로드 실패");
            result.put("success", false);
        }
        return result;
    }

    @GetMapping("/getTProduct/{page}")
    public HashMap<String, Object> getTProduct(
            @PathVariable("page") int page,
            @RequestParam(required = false) String search
    ) {
        return as.getTProduct(page, search);
    }


    @GetMapping("/AdminTProduct/{tid}")
    public HashMap<String, Object> getAdminTProduct(@PathVariable("tid") int tid) {
        return as.getAdminTProduct(tid);
    }

    @PostMapping("/updateAdminTProduct")
    public HashMap<String, Object> updateAdminTProduct(
            @RequestParam int tid,
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam String company,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) Integer starttime,
            @RequestParam(required = false) Integer endtime,
            @RequestParam int maxcount,
            @RequestParam int price1,
            @RequestParam int price2,
            @RequestParam int salecount,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        HashMap<String, Object> result = new HashMap<>();
        boolean updateCheck = as.updateAdminTProduct(
                tid, name, category, company, start, end,
                starttime != null ? starttime : 0,
                endtime != null ? endtime : 0,
                maxcount, price1, price2, salecount, imageFile
        );
        result.put("msg", updateCheck ? "ok" : "fail");
        return result;
    }



    @PostMapping(value = "/insertAdminTProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HashMap<String, Object> insertAdminTProduct(
            @RequestParam String name,
            @RequestParam String category,
            @RequestParam String company,
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String starttime,
            @RequestParam String endtime,
            @RequestParam int maxcount,
            @RequestParam Integer salecount,
            @RequestParam int price1,
            @RequestParam int price2,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        HashMap<String, Object> result = new HashMap<>();

        boolean ok = as.insertAdminTProduct(
                name, category, company, start, end, starttime, endtime,
                maxcount, price1, price2, salecount, imageFile
        );

        result.put("msg", ok ? "ok":"fail");
        return result;
    }




    @PostMapping("/updateAdmin")
    public HashMap<String, Object> updateAdmin(@RequestParam("mid") int mid){
        HashMap<String, Object> result = as.updateAdmin(mid);
        return result;
    }


    @PostMapping("/updateUser")
    public HashMap<String , Object> updateUser(@RequestParam("mid") int mid){
        HashMap<String, Object> result = as.updateUser(mid);
        return result;
    }


    @PostMapping("/goSerch")
    public HashMap<String, Object> goSerch(@RequestParam("key") String key){
        HashMap<String, Object> result = as.getSerchResult(key);
        return result;
    }

    @PostMapping("/deleteEProduct")
    public HashMap<String, Object> deleteEProduct(@RequestBody List<Integer> checkList){
        HashMap<String, Object> result = new HashMap<>();
        as.deleteEProduct(checkList);
        result.put("result", "success");
        return result;
    }

    @PostMapping("/deleteHProduct")
    public HashMap<String, Object> deleteHProduct(@RequestBody List<Integer> checkList){
        HashMap<String, Object> result = new HashMap<>();
        as.deleteHProduct(checkList);
        result.put("result", "success");
        return result;
    }

    @PostMapping("/deleteTProduct")
    public HashMap<String, Object> deleteTProduct(@RequestBody List<Integer> checkList){
        HashMap<String, Object> result = new HashMap<>();
        as.deleteTProduct(checkList);
        result.put("result", "success");
        return result;
    }

    @GetMapping("/getAdminHOption")
    public HashMap<String, Object> getAdminHOption(@RequestParam("hid")int hid) {
        HashMap<String, Object> result = as.getOptionByHid(hid);
        return result;
    }

    @PostMapping("/updateAdminHOption")
    public void updateAdminHOption(@RequestBody Options options){

        as.updateAdminHOption(options);
    }


}
