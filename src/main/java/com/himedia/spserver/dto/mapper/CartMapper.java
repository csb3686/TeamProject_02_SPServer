package com.himedia.spserver.dto.mapper;


import com.himedia.spserver.dto.request.CartGetListRequest;
import com.himedia.spserver.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper {

    public CartGetListRequest toMapper(Cart c, Object cartDTO) {

        CartGetListRequest cartdto = new CartGetListRequest();
        cartdto.setCartid(c.getCartid());
        cartdto.setOpid(String.valueOf(c.getOpid()));
        cartdto.setCategory(c.getCategory());
        cartdto.setPid(c.getPid());
        cartdto.setCount(c.getCount());
        cartdto.setPrice(c.getPrice());
        cartdto.setImage(c.getImage());
        cartdto.setSelecteddate(c.getSelecteddate());
        cartdto.setCheckInDate(c.getCheckInDate());
        cartdto.setCheckOutDate(c.getCheckOutDate());
        cartdto.setCid(c.getCid());

        cartdto.setProduct(cartDTO);

        return cartdto;
    }

}


