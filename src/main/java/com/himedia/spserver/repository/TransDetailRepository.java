package com.himedia.spserver.repository;

import com.himedia.spserver.entity.TransDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransDetailRepository extends JpaRepository<TransDetail, Integer> {

    List<TransDetail> findTop4ByOrderBySalecountDesc();

    TransDetail findByTid(int pid);


    List<TransDetail> findByNameContainingOrStartContainingOrEndContaining(
            String name, String start, String end
    );

    Page<TransDetail> findByNameContainingOrStartContainingOrEndContaining(
            String name, String start, String end,
            Pageable pageable
    );

    List<TransDetail> findAllByCid(String cid);

    List<TransDetail> findAllByEndContainingAndStartContaining(String city, String start);

    @Query("SELECT h FROM TransDetail h WHERE REPLACE(h.name, ' ', '') LIKE %:keyword%")
    List<TransDetail> findNameContaining(@Param("keyword") String normalizedKey);
}

