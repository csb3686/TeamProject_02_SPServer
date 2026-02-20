package com.himedia.spserver.service;

import com.himedia.spserver.dto.mapper.OrdersMapper;
import com.himedia.spserver.dto.request.OrderRequest;
import com.himedia.spserver.dto.request.OrdersGetListRequest;
import com.himedia.spserver.dto.response.OrderDetailResponse;
import com.himedia.spserver.entity.*;
import com.himedia.spserver.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cr;
    private final OrdersRepository odr;
    private final OrdersMapper om;
    private final HotelRepository hr;
    private final OptionsRepository or;
    private final TransDetailRepository tdr;
    private final ExperienceRepository er;
    private final MemberRepository mr;
    private final TimetableRepository tr;
    private final ReviewRepository rr;

    // ----------------------------
    // HH:mm 변환 함수 (교통에서 사용)
    // ----------------------------
    private String formatTime(Integer timeValue) {
        if (timeValue == null) return "-";
        int hour = timeValue / 100;
        int minute = timeValue % 100;
        return String.format("%02d:%02d", hour, minute);
    }


    // ----------------------------
    // 주문 생성
    // ----------------------------
    public Orders insertOrder(OrderRequest dto) {

        Member member = mr.findByUserid(dto.getUserid());
        Orders order = null;

        switch (dto.getCategory()) {

            case "숙소":
                Hotel hotel = hr.findById(dto.getPid())
                        .orElseThrow(() -> new RuntimeException("Hotel not found"));
                Options option = or.findById(Integer.valueOf(dto.getOpid()))
                        .orElseThrow(() -> new RuntimeException("Option not found"));

                long nights = ChronoUnit.DAYS.between(
                        dto.getCheckInDate().toLocalDateTime().toLocalDate(),
                        dto.getCheckOutDate().toLocalDateTime().toLocalDate()
                );

                order = new Orders();
                order.setMid(member.getMid());
                order.setCid(hotel.getCid());
                order.setCategory("숙소");
                order.setPid(dto.getPid());
                order.setOpid(option.getOpid());
                order.setPrice((int) (option.getPrice1() * nights * dto.getCount()));
                order.setCount(dto.getCount());
                order.setCheckInDate(dto.getCheckInDate());
                order.setCheckOutDate(dto.getCheckOutDate());
                order.setSelecteddate(dto.getCheckInDate());

                option.setSalecount(option.getSalecount() + 1);
                break;

            case "교통":
                TransDetail trans = tdr.findById(dto.getPid())
                        .orElseThrow(() -> new RuntimeException("Transport not found"));

                order = new Orders();
                order.setMid(member.getMid());
                order.setCid(trans.getCid());
                order.setCategory("교통");
                order.setPid(dto.getPid());
                order.setPrice(trans.getPrice1() * dto.getCount());
                order.setCount(dto.getCount());
                order.setSelecteddate(dto.getSelecteddate());

                trans.setSalecount(trans.getSalecount() + 1);
                break;

            case "체험":
                Experience exp = er.findById(dto.getPid())
                        .orElseThrow(() -> new RuntimeException("Experience not found"));

                order = new Orders();
                order.setMid(member.getMid());
                order.setCid(exp.getCid());
                order.setCategory("체험");
                order.setPid(dto.getPid());
                order.setPrice(exp.getPrice1() * dto.getCount());
                order.setCount(dto.getCount());
                order.setSelecteddate(dto.getSelecteddate());

                exp.setSalecount(exp.getSalecount() + 1);
                break;

            default:
                throw new RuntimeException("Invalid category: " + dto.getCategory());
        }

        // 공통 필드
        order.setMname(dto.getMname());
        order.setMphone(dto.getMphone());
        order.setIndate(Timestamp.valueOf(LocalDateTime.now()));

        return odr.save(order);
    }


    // ----------------------------
    // 주문 리스트
    // ----------------------------
    public List<OrdersGetListRequest> getOrderList(String userid) {
        Member member = mr.findByUserid(userid);
        List<Orders> ordersList = odr.findAllByMidOrderByIndateAsc(member.getMid());

        List<OrdersGetListRequest> result = new ArrayList<>();

        for (Orders o : ordersList) {
            Object productDTO = null;
            Options options = null;

            switch (o.getCategory()) {
                case "숙소" -> {
                    options = or.findByOpid(o.getOpid());
                    productDTO = hr.findByHid(options.getHid());
                }
                case "교통" -> productDTO = tdr.findByTid(o.getPid());
                case "체험" -> productDTO = er.findByEid(o.getPid());
            }

            OrdersGetListRequest odto = om.toMapper(o, productDTO, options);
            odto.setHasReview(rr.existsByOid(o.getOid()));
            result.add(odto);
        }

        return result;
    }


    // ----------------------------
    // 주문 상세
    // ----------------------------
    public OrderDetailResponse getOrderDetail(int oid) {
        Orders order = odr.findById(oid).orElseThrow();
        OrderDetailResponse dto = new OrderDetailResponse();
        dto.setOrder(order);

        switch (order.getCategory()) {

            // ---------------- 숙소 ----------------
            case "숙소":
                Options option = or.findById(order.getOpid()).orElseThrow();
                Hotel hotel = hr.findById(order.getPid()).orElseThrow();
                dto.setOption(option);
                dto.setHotel(hotel);
                break;

            // ---------------- 체험 ----------------
            case "체험":
                Experience exp = er.findById(order.getPid()).orElseThrow();
                dto.setExperience(exp);
                break;

            // ---------------- 교통 ----------------
            case "교통":
                TransDetail trans = tdr.findById(order.getPid()).orElseThrow();
                dto.setTrans(trans);

                dto.setStarttime(formatTime(trans.getStarttime()));
                dto.setEndtime(formatTime(trans.getEndtime()));
                break;
        }

        return dto;
    }
}
