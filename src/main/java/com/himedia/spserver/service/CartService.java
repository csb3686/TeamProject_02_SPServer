package com.himedia.spserver.service;

import com.himedia.spserver.dto.mapper.CartMapper;
import com.himedia.spserver.dto.mapper.HotelMapper;
import com.himedia.spserver.dto.request.CartGetListRequest;
import com.himedia.spserver.dto.request.CartInsertRequest;
import com.himedia.spserver.dto.response.CartListResponse;
import com.himedia.spserver.entity.*;
import com.himedia.spserver.repository.*;
import com.himedia.spserver.util.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

   // private final MemberRepository mr;
    private final CartRepository cr;
    private final OptionsRepository or;
    private final HotelRepository hr;
    private final TransDetailRepository tr;
    private final ExperienceRepository er;
    private final MemberRepository mr;
    private final OrdersRepository odr;

    private final CartMapper cm;
    private final HotelMapper hm;


    public LocalDate getCheckOutDate(Timestamp timestamp) {
        if (timestamp == null) return null;

        return timestamp.toLocalDateTime().toLocalDate();
    }


    public void insertCart(CartInsertRequest dto) {
        // (공통)  회원 userid조회
        Member member = mr.findByUserid(dto.getUserid());


//        //category별로 분기처리
        switch(dto.getCategory()) {
            case "숙소":
                // db에서 상품정보들 가져옴
                Hotel hotel = hr.findById(dto.getPid())
                        .orElseThrow(() -> new RuntimeException("Hotel not found"));
                Options option = or.findById(Integer.valueOf(dto.getOpid()))
                        .orElseThrow(() -> new RuntimeException("Option not found"));

                // 숙소는 체크인/체크아웃 날짜 차이로 박수 계산
                long nights = ChronoUnit.DAYS.between(
                        dto.getCheckInDate().toLocalDateTime().toLocalDate(),
                        dto.getCheckOutDate().toLocalDateTime().toLocalDate()
                );

                // Cart 세팅 (이부분에 각케이스마다 cart테이블에 들어가야하는값 넣어줌)
                Cart hotelCart = new Cart();
                hotelCart.setCid(hotel.getCid());
                hotelCart.setCategory("숙소");
                hotelCart.setPid(dto.getPid());
                hotelCart.setOpid(option.getOpid());
                hotelCart.setName(hotel.getName());
                hotelCart.setOpname(option.getName());
                hotelCart.setPrice((int)(option.getPrice1() * nights  * dto.getCount())); // 박수 * 객실수
                hotelCart.setCount(dto.getCount());// 객실수

                hotelCart.setMid(member.getMid());
                hotelCart.setUserid(member.getUserid());
                hotelCart.setImage(option.getImage());

                hotelCart.setCheckInDate(dto.getCheckInDate());
                hotelCart.setCheckOutDate(dto.getCheckOutDate());
                ;
                cr.save(hotelCart);

                break;

            case "교통":
                TransDetail trans = tr.findById(dto.getPid())
                        .orElseThrow(() -> new RuntimeException("Transport not found"));
                // Cart 세팅
                Cart transCart = new Cart();
                transCart.setCid(trans.getCid());
                transCart.setCategory("교통");
                transCart.setPid(dto.getPid());
                //transCart.setOpid(0); // 옵션 없음
                transCart.setName(trans.getName());
                //transCart.setOpname(null); // 옵션명 없음
                transCart.setPrice(trans.getPrice1() * dto.getCount()); // 총 금액
                transCart.setCount(dto.getCount());
                transCart.setMid(member.getMid());
                transCart.setUserid(member.getUserid());
                transCart.setImage(trans.getImage());
                transCart.setSelecteddate(dto.getSelecteddate());
                //transCart.setCheckInDate(null);
                //transCart.setCheckOutDate(null);

                cr.save(transCart);
                break;

            case "체험":
                Experience exp = er.findById(dto.getPid())
                        .orElseThrow(() -> new RuntimeException("Experience not found"));
                // Cart 세팅
                Cart expCart = new Cart();
                expCart.setCid(exp.getCid());
                expCart.setCategory("체험");
                expCart.setPid(dto.getPid());
                //expCart.setOpid(0); // 옵션 없음
                expCart.setName(exp.getName());
                //expCart.setOpname(null);
                expCart.setPrice(exp.getPrice1() * dto.getCount()); // 총 금액
                expCart.setCount(dto.getCount());
                expCart.setMid(member.getMid());
                expCart.setUserid(member.getUserid());
                expCart.setImage(exp.getImage());
                expCart.setSelecteddate(dto.getSelecteddate()); // 체험일
                //expCart.setCheckInDate(null);
                //expCart.setCheckOutDate(null);

                cr.save(expCart);
                break;

            default:
                throw new RuntimeException("Invalid category: " + dto.getCategory());
                //잘못된 카테고리 값이 들어오면 무시하지 말고 바로 에러로 알리기
        }
    }

    public CartListResponse getCartList(String userid, int page) {
        List<Cart> cartlist = cr.findByUserid(userid);
        List<CartGetListRequest> result = new ArrayList<>();
        List<Cart> deletedList = new ArrayList<>();

        LocalDate now = LocalDate.now();

        for(Cart c : cartlist) {

            // 날짜 비교 (숙소는 checkOutDate 기준, 체험은 selectedDate 기준 등)
            boolean isExpired = false;

            LocalDate checkIn = getCheckOutDate(c.getCheckInDate());
            LocalDate checkOut = getCheckOutDate(c.getCheckOutDate());
            LocalDate selected = getCheckOutDate(c.getSelecteddate());

            switch (c.getCategory()) {
                case "숙소" -> {
                    // checkout 날짜 기준
                    if (checkOut != null && checkOut.isBefore(now)) {
                        isExpired = true;
                    }
                }
                case "교통", "체험" -> {
                    // selected 날짜 기준
                    if (selected != null && selected.isBefore(now)) {
                        isExpired = true;
                    }
                }
            }

            // 만료된 상품이면 삭제 리스트에 추가하고 result에는 추가하지 않음
            if (isExpired) {
                deletedList.add(c);
                continue;
            }

            // 정상 처리되는 경우 (기존 그대로)
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
            result.add(cdto);
        }

        // DB 삭제
        if (!deletedList.isEmpty()) {
            cr.deleteAll(deletedList);
        }

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(6);
        paging.setDisplayRow(6);

        int count = cartlist.size();
        paging.setTotalCount(count);
        paging.calPaging();

        int start = paging.getStartNum() -1;
        if (start >= result.size()) {
            // 시작 인덱스가 리스트 길이보다 크면 빈 리스트 반환
            return new CartListResponse();
        }
        int end = Math.min(start + paging.getDisplayRow(), result.size());

        List<CartGetListRequest> pagedList = result.subList(start, end);


        CartListResponse response = new CartListResponse();
        response.setCartList(pagedList);
        response.setDeletedList(deletedList);
        response.setPaging(paging);

        return response;
    }

    public void deleteCart(int cartid) {
        cr.deleteByCartid(cartid);
    }

    public List<Cart> getCartForOrder(List<Integer> cidList) {

        List<Cart> result = cr.findByCartidIn(cidList);
        return result;
    }

    public void insertOrderByCart(List<CartGetListRequest> cartList, String userid, String mname, String mphone) {

        Member member = mr.findByUserid(userid);

        System.out.println("체크인아웃 데이트가 안들어가네"+cartList);

        for (CartGetListRequest cart : cartList) {

            Orders orders = new Orders();

            orders.setCid(cart.getCid());
            orders.setCategory(cart.getCategory());
            orders.setCount(cart.getCount());
            orders.setMid(member.getMid());
            orders.setMname(mname);
            orders.setMphone(mphone);
            orders.setPid(cart.getPid());
            orders.setPrice(cart.getPrice());
            orders.setCid(String.valueOf(cart.getCid()));
            orders.setPackageOrder(cart.getIspackage());

            if(cart.getCategory().equals("숙소")){
                Options op = or.findByOpid(Integer.parseInt(cart.getOpid()));
                System.out.println("카트에 있는 옵션아이디 : " + cart.getPid());
                System.out.println("그 옵션아이디로 찾은 옵션 : "+op);
                String opidStr = cart.getOpid();
                if (opidStr != null && !opidStr.isEmpty()) {
                    orders.setOpid(Integer.parseInt(opidStr));
                } else {
                    orders.setOpid(0); // 기본값
                }
                orders.setSelecteddate(cart.getCheckInDate());
                orders.setCheckInDate(cart.getCheckInDate());
                orders.setCheckOutDate(cart.getCheckOutDate());

                op.setSalecount(op.getSalecount() + 1);
            } else {
                orders.setSelecteddate(cart.getSelecteddate());
                orders.setOpid(0);
            }

            if(cart.getCategory().equals("교통")){
                TransDetail td = tr.findByTid(cart.getPid());
                td.setSalecount(td.getSalecount() + 1);
            }

            if(cart.getCategory().equals("체험")){
                Experience ex = er.findByEid(cart.getPid());
                ex.setSalecount(ex.getSalecount() + 1);
            }

            odr.save(orders);

            cr.deleteByCartid(cart.getCartid());

        }
    }


    public Integer getCartCount(String userid) {
        if(userid == null || userid.isEmpty()) {
            return 0; // 혹은 예외 처리
        }
        return cr.countByUserid(userid);
    }

    public List<String> removeDeletedItems(String userid) {
        List<Cart> cartList = cr.findByUserid(userid);
        List<Cart> toDelete = new ArrayList<>();
        List<String> deletedNames = new ArrayList<>();

        for (Cart c : cartList) {
            boolean isDeleted = switch (c.getCategory()) {
                case "숙소" -> !hr.existsById(c.getPid());
                case "교통" -> !tr.existsById(c.getPid());
                case "체험" -> !er.existsById(c.getPid());
                default -> false;
            };

            if (isDeleted) {
                toDelete.add(c);
                deletedNames.add(c.getName()); // 삭제된 상품 이름 수집
            }
        }

        if (!toDelete.isEmpty()) {
            cr.deleteAll(toDelete);
        }

        return deletedNames; // 이름 리스트 반환
    }
}
