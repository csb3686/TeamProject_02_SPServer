package com.himedia.spserver.dto.mapper;

import com.himedia.spserver.dto.request.OrdersGetListRequest;
import com.himedia.spserver.entity.Hotel;
import com.himedia.spserver.entity.Options;
import com.himedia.spserver.entity.Orders;
import jakarta.validation.constraints.Null;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdersMapper {

    public OrdersGetListRequest toMapper(Orders orders, Object productDTO, Options options) {
        OrdersGetListRequest orderdto = new OrdersGetListRequest();
        orderdto.setOid(orders.getOid());
        orderdto.setMname(orders.getMname());
        orderdto.setMphone(orders.getMphone());
        orderdto.setCount(orders.getCount());
        orderdto.setCategory(orders.getCategory());
        orderdto.setIndate(orders.getIndate());
        orderdto.setSelecteddate(orders.getSelecteddate());

        //이거추가
        orderdto.setCheckInDate(orders.getCheckInDate());
        orderdto.setCheckOutDate(orders.getCheckOutDate());

        if(options==null){
            orderdto.setOpid(0);
            orderdto.setName("");
            orderdto.setPrice1(0);
        }else{
            orderdto.setOpid(options.getOpid());
            orderdto.setName(options.getName());
            orderdto.setPrice1(options.getPrice1());
        }


        orderdto.setProduct(productDTO);

        return orderdto;
    }


}
