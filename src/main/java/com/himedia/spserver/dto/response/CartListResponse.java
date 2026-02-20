package com.himedia.spserver.dto.response;

import com.himedia.spserver.dto.request.CartGetListRequest;
import com.himedia.spserver.entity.Cart;
import com.himedia.spserver.util.Paging;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CartListResponse {
    private List<CartGetListRequest> cartList;
    private List<Cart> deletedList;

    private Paging paging;
}
