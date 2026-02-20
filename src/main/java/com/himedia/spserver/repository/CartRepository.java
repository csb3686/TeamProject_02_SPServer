package com.himedia.spserver.repository;

import com.himedia.spserver.dto.request.CartGetListRequest;
import com.himedia.spserver.entity.Cart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {



    void deleteByCartid(int cartid);

    List<Cart> findByCartidIn(List<Integer> cidList);

    List<Cart> findAllByIspackage(int packageid);

    Integer countByUserid(String userid);

    List<Cart> findByUserid(String userid);
}
