package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    Notice findByNid(int nid);

    @Query("SELECT h FROM Notice h WHERE REPLACE(h.title, ' ', '') LIKE %:keyword1% and REPLACE(h.content, ' ', '') like %:keyword2%")
    List<Notice> findByTitleOrContentContaining(@Param("keyword1") String normalizedKey, @Param("keyword2") String normalizedKey1);
}
