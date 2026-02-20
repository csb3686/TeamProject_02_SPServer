package com.himedia.spserver.dto.request;

import com.himedia.spserver.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartGetListRequest {

    private int cartid;
    private String cid; // 도시id
    private String opid;
    private String category;
    private int count;
    private int pid;
    private int price;
    private Timestamp selecteddate;
    private String image;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;
    private int ispackage;

    private Object product;

    private List<Integer> cidList; // cartid 리스트

    private List<CartGetListRequest> cartList;

}
