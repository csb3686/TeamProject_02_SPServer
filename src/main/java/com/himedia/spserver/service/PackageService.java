package com.himedia.spserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.himedia.spserver.dto.mapper.CartMapper;
import com.himedia.spserver.dto.mapper.HotelMapper;
import com.himedia.spserver.dto.request.CartGetListRequest;
import com.himedia.spserver.dto.response.ExList;
import com.himedia.spserver.dto.response.HotelDTO;
import com.himedia.spserver.dto.response.OptionDTO;
import com.himedia.spserver.dto.response.PackageInfoDto;
import com.himedia.spserver.entity.*;
import com.himedia.spserver.entity.Packagelist;
import com.himedia.spserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final CityRepository cr;
    private final TransDetailRepository tdr;
    private final HotelRepository hr;
    private final OptionsRepository or;
    private final ExperienceRepository exr;
    private final CartRepository ctr;
    private final MemberRepository mr;
    private final PackageListRepository pkr;
    private final TransDetailRepository tr;
    private final ExperienceRepository er;

    private final CartMapper cm;
    private final HotelMapper hm;

    public String getCidByPlace(String keyword) {

        System.out.println("이걸로 찾아라"+keyword);
        City city1 = cr.findFirstCidByAd1ContainingOrAd2Containing(keyword, keyword);

        System.out.println("도시정보" + city1);

        return city1.getCid();
    }

    public HashMap<String, Object> getTransListByCid(String cid) {
        HashMap<String, Object> result = new HashMap<>();
        List<TransDetail> transe = tdr.findAllByCid(cid);
        result.put("transList", transe);
        return result;
    }

    public HashMap<String, Object> getTransListByPlace(String city, String start) {
        HashMap<String, Object> result = new HashMap<>();
        List<TransDetail> transe = tdr.findAllByEndContainingAndStartContaining(city, start);


        result.put("transList", transe);
        return result;
    }

    private String formatTime(Integer timeValue) {
        if (timeValue == null) return "-";

        int hour = timeValue / 100;
        int minute = timeValue % 100;

        return String.format("%02d:%02d", hour, minute);
    }

    public HashMap<String, Object> getHotelListByCid(String cid) {
        HashMap<String, Object> result = new HashMap<>();

        List<Hotel> hotel = hr.findByCidIn(cid);
        result.put("hotelList", hotel);
        return result;
    }

    public HashMap<String, Object> getOptionListByHid(int hid) {
        HashMap<String, Object> result = new HashMap<>();

        List<Options> option = or.findAllByHid(hid);
        result.put("optionList", option);
        return result;
    }

    public HashMap<String, Object> getExListByCid(String cid) {
        HashMap<String, Object> result = new HashMap<>();

        List<Experience> ex = exr.findByCidIn(cid);

        result.put("exList", ex);
        return result;
    }

    public HashMap<String, Object> insertCartPackage(PackageInfoDto pdto) throws JsonProcessingException {

        HashMap<String, Object> result = new HashMap<>();
        System.out.println("pdto 어떤게 왔나요? : " + pdto);

        // userid로 회원 조회
        Member member = mr.findByUserid(pdto.getUserid());

        Packagelist p = new Packagelist();
        p.setMid(member.getMid());
        pkr.save(p);

        // mid로 packageid 조회
        // 찾아온 데이터들 중 가장 높은 packageid로 가져오기
        int packageid = pkr.findMaxIdByMid(member.getMid());


        if(pdto.getHid()!=0){
            // hid, opid로 숙소 정보 조회
            Options option = or.findByOpid(pdto.getOpid());
            if(option.getHid() != pdto.getHid()){
                System.out.println("뭔가 잘못됨");
            }
            Hotel hotel = hr.findByHid(pdto.getHid());

            // insert 하기
            Cart cartForHotel = new Cart();
            cartForHotel.setCid(pdto.getCid());
            cartForHotel.setCategory("숙소");
            cartForHotel.setImage(hotel.getImage());
            cartForHotel.setCount(pdto.getHotelcount());
            cartForHotel.setName(hotel.getName());
            cartForHotel.setOpid(option.getOpid());
            cartForHotel.setOpname(option.getName());
            cartForHotel.setPid(option.getHid());
            cartForHotel.setPrice(option.getPrice1());
            cartForHotel.setSelecteddate(pdto.getStartdate());
            cartForHotel.setMid(member.getMid());
            cartForHotel.setUserid(member.getUserid());
            cartForHotel.setCheckInDate(pdto.getStartdate());
            cartForHotel.setCheckOutDate(pdto.getEnddate());
            cartForHotel.setIspackage(packageid);

            ctr.save(cartForHotel);
        }

        /*  여기는 starttrans 데이터 조회, 저장하는 곳  */
        if(pdto.getSttid() != 0){
            // tid로 교통 정보 조회
            TransDetail starttrans = tdr.findByTid(pdto.getSttid());

            // insert 하기
            Cart cartForTrans = new Cart();
            cartForTrans.setCid(pdto.getCid());
            cartForTrans.setCategory("교통");
            cartForTrans.setImage(starttrans.getImage());
            cartForTrans.setCount(pdto.getSttranscount());
            cartForTrans.setName(starttrans.getName());
            cartForTrans.setPid(starttrans.getTid());
            cartForTrans.setPrice(starttrans.getPrice1());
            cartForTrans.setSelecteddate(pdto.getSttransselecteddate());
            cartForTrans.setMid(member.getMid());
            cartForTrans.setUserid(member.getUserid());
            cartForTrans.setIspackage(packageid);

            ctr.save(cartForTrans);
        }
        /*  여기는 starttrans 데이터 조회, 저장하는 곳  */


        /*  여기는 endtrans 데이터 조회, 저장하는 곳  */
        if(pdto.getEntid() != 0){
            // tid로 교통 정보 조회
            TransDetail endtrans = tdr.findByTid(pdto.getEntid());

            // insert 하기
            Cart cartForTrans = new Cart();
            cartForTrans.setCid(pdto.getCid());
            cartForTrans.setCategory("교통");
            cartForTrans.setImage(endtrans.getImage());
            cartForTrans.setCount(pdto.getSttranscount());
            cartForTrans.setName(endtrans.getName());
            cartForTrans.setPid(endtrans.getTid());
            cartForTrans.setPrice(endtrans.getPrice1());
            cartForTrans.setSelecteddate(pdto.getEntransselecteddate());
            cartForTrans.setMid(member.getMid());
            cartForTrans.setUserid(member.getUserid());
            cartForTrans.setIspackage(packageid);

            ctr.save(cartForTrans);
        }

        /*  여기는 endtrans 데이터 조회, 저장하는 곳  */
        result.put("packageid", packageid);
        return result;
    }

    public HotelDTO getHotelByHid(int hid) {
        Hotel h = hr.findNameByHid(hid);
        HotelDTO hotel = new HotelDTO();
        hotel.setName(h.getName());
        return hotel;
    }

    public OptionDTO getOptionByOpid(int opid) {
        Options op = or.findByOpid(opid);
        OptionDTO option = new OptionDTO();
        option.setPrice(op.getPrice1());
        option.setName(op.getName());
        return option;
    }

    public void insertCartExlist(ExList exlist, int packageid, String userid) {
        System.out.println("exlist에는 이런 내용이 왔어요 : "+exlist);

        Member member =  mr.findByUserid(userid);
        System.out.println("날짜가 안들어감 : "+exlist);

        if(exlist != null){

            String cid = exlist.getCid().substring(0, 2);

            Cart cart = new Cart();
            cart.setIspackage(packageid);
            cart.setUserid(userid);
            cart.setMid(member.getMid());
            cart.setCid(cid);
            cart.setCategory("체험");
            cart.setPid(exlist.getEid());
            cart.setSelecteddate(exlist.getSelectedDate());
            cart.setPrice(exlist.getPrice2());
            cart.setCount(exlist.getCount());
            cart.setName(exlist.getName());
            cart.setImage(exlist.getImage());

            ctr.save(cart);
        }



    }

    public List<CartGetListRequest> getCartList(int packageid) {

        List<Cart> cartlist = ctr.findAllByIspackage(packageid);
        List<CartGetListRequest> result = new ArrayList<>();

        System.out.println("제대로 오고있니 pkid? : "+packageid);

        System.out.println("cartList : "+cartlist);

        for(Cart c : cartlist) {
            Object cartDTO = null;

            switch (c.getCategory()){
                case "숙소" -> {
                    Options option = or.findHidByOpid(c.getOpid());
                    Hotel hotel = hr.findByHid(c.getPid());
                    cartDTO = hm.toMapper(hotel, c.getOpid());
                }
                case "교통" -> cartDTO = tr.findByTid(c.getPid());
                case "체험" -> cartDTO = er.findByEid(c.getPid());
            }

            CartGetListRequest cdto = cm.toMapper(c, cartDTO);

            System.out.println("대체 어디서 없어지는것임" + cdto);
            result.add(cdto);
        }

        return result;
    }
}
