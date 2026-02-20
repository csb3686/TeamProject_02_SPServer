package com.himedia.spserver.repository;

import com.himedia.spserver.dto.response.HotelDTO;
import com.himedia.spserver.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Integer> {
  
    List<Hotel> findTop4ByOrderByViewcountDesc();

    Hotel findByHid(int pid);


    Page<Hotel> findByNameContainingOrContentContaining(String name, String content, Pageable pageable);
    List<Hotel> findByNameContainingOrContentContaining(String name, String content);
    List<Hotel> findAll();

    @Query("SELECT h FROM Hotel h WHERE str(h.cid) LIKE %:cid%")
    List<Hotel> findByCidIn(@Param("cid")String cid);

    @Query("SELECT h FROM Hotel h WHERE REPLACE(h.name, ' ', '') LIKE %:keyword%")
    List<Hotel> findByNameContaining(@Param("keyword") String key);

    Hotel findTopByOrderByHidDesc();

    Hotel findNameByHid(int hid);
}
