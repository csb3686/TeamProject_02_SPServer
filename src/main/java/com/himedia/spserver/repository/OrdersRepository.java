package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {


    @Query("SELECT o.cid FROM Orders o " +
            "WHERE o.indate >= :startDate " +
            "GROUP BY o.cid " +
            "ORDER BY COUNT(o.cid) DESC")
    List<Integer> findTopCidLastMonth(LocalDateTime startDate, Pageable pageable);

    @Query("SELECT o.pid FROM Orders o " +
            "WHERE o.cid = :cid AND o.category = :category " +
            "GROUP BY o.pid " +
            "ORDER BY COUNT(o.pid) DESC")
    List<Integer> findTopPidByCidAndCategory(String cid, String category);

    List<Orders> findAllByMidOrderByIndateAsc(int mid);

    @Query("""
        select coalesce(sum(o.count), 0)
        from Orders o
        where o.category = '교통'
          and o.pid = :tid
          and function('date', o.selecteddate) = :selectDate
    """)
    Integer countTrans(@Param("tid") int tid, @Param("selectDate") LocalDate selectDate);
}
