package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findTop4ByOrderByPointDesc();

    @Query("""
    SELECT r, h.name
    FROM Review r
    LEFT JOIN Hotel h ON r.hid = h.hid
    WHERE r.category = :category
""")
    Page<Object[]> findReviewWithHotel(
            @Param("category") String category,
            Pageable pageable);

    @Query("""
    SELECT r, e.name
    FROM Review r
    LEFT JOIN Experience e ON r.eid = e.eid
    WHERE r.category = :category
""")
    Page<Object[]> findReviewWithEx(
            @Param("category") String category,
            Pageable pageable);

    @Query("""
    SELECT r, t.name
    FROM Review r
    LEFT JOIN TransDetail t ON r.tid = t.tid
    WHERE r.category = :category
""")
    Page<Object[]> findReviewWithTrans(
            @Param("category") String category,
            Pageable pageable);

    List<Review> findTop4ByCategoryOrderByPointDesc(String category);

    List<Review> findAllByTid(int tid);

    List<Review> findAllByHid(int hid);

    List<Review> findAllByEid(int eid);

    boolean existsByOid(int oid); //오더아이디 기준 리뷰 존재 여부 체크


    Page<Review> findByUserid(String userid, Pageable pageable);
}
