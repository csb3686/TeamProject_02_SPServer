package com.himedia.spserver.controller;

import com.himedia.spserver.dto.mapper.OrdersMapper;
import com.himedia.spserver.dto.request.OrderRequest;
import com.himedia.spserver.dto.request.OrdersGetListRequest;
import com.himedia.spserver.dto.response.OrderDetailResponse;
import com.himedia.spserver.entity.Cart;
import com.himedia.spserver.entity.Hotel;
import com.himedia.spserver.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.himedia.spserver.entity.Orders;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService ods;



    @PostMapping("/insertOrder")
    public HashMap<String,Object> insertOrder(@RequestBody OrderRequest dto){
        HashMap<String,Object> result = new HashMap<>();
        Orders order = ods.insertOrder(dto);
        //해당주문 카트에서 삭제
        result.put("oid",order.getOid());
        result.put("msg","ok");
        return result;
    }

    @GetMapping("/getOrderDetail/{oid}")
    public OrderDetailResponse getOrderDetail(@PathVariable int oid) {
        OrderDetailResponse dto = ods.getOrderDetail(oid);


        return dto;
    }

    @GetMapping("/getOrderList")
    public ResponseEntity<List<OrdersGetListRequest>> getOrderList(@RequestParam("userid") String userid) {
        List<OrdersGetListRequest> orderList = ods.getOrderList(userid);
        return  ResponseEntity.ok(orderList);
    }



}
