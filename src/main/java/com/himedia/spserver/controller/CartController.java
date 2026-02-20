package com.himedia.spserver.controller;

import com.himedia.spserver.dto.request.CartGetListRequest;
import com.himedia.spserver.dto.request.CartInsertRequest;
import com.himedia.spserver.dto.response.CartListResponse;
import com.himedia.spserver.entity.Cart;
import com.himedia.spserver.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cs;

    @PostMapping("/insertCart")
    public HashMap<String, Object> insertCart(@RequestBody CartInsertRequest dto) {
        HashMap<String,Object> result = new HashMap<>();
        cs.insertCart(dto);
        result.put("msg", "ok");
        return result;
    }

    @GetMapping("/getCartList")
    public ResponseEntity<CartListResponse> getCartList(@RequestParam("userid") String userid, @RequestParam("page")int page) {
        CartListResponse cartList = cs.getCartList(userid, page);
        return  ResponseEntity.ok(cartList);
    }

    @DeleteMapping("/deleteCart/{cartid}")
    public void deleteCart(@PathVariable int cartid) {
        cs.deleteCart(cartid);
    }

    @PostMapping("/getOrdersFromCart")
    public ResponseEntity<List<Cart>> getOrdersFromCart(@RequestBody CartGetListRequest dto) {
        List<Integer> cidList = dto.getCidList();
        List<Cart> cartList = cs.getCartForOrder(cidList);
        return ResponseEntity.ok(cartList);
    }

    @PostMapping("/insertOrderbyCart")
    public ResponseEntity<HashMap<String, Object>> insertOrderbyCart(@RequestBody CartGetListRequest dto, @RequestParam("userid") String userid, @RequestParam("mname") String mname, @RequestParam("mphone") String mphone) {
        HashMap<String, Object> result = new HashMap<>();
        List<CartGetListRequest> cartList = dto.getCartList();
        cs.insertOrderByCart(cartList, userid, mname, mphone);
        result.put("msg", "ok");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getCartCount")
    public Integer getCartCount(@RequestParam("userid") String userid) {
        return cs.getCartCount(userid);
    }

    @DeleteMapping("/removeDeletedItems")
    public ResponseEntity<List<String>> removeDeletedItems(@RequestParam String userid) {
        List<String> deletedNames = cs.removeDeletedItems(userid);
        return ResponseEntity.ok(deletedNames);
    }

}
