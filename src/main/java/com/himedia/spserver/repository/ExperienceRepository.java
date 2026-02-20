package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {

    List<Experience> findAll();

    Experience findByEid(int pid);


    Page<Experience> findByNameContainingOrContentContaining(String name, String content, Pageable pageable);

    @Query("SELECT e \n" +
            "FROM Experience e \n" +
            "WHERE SUBSTRING(str(e.cid), 1, 2) = :cid")
    List<Experience> findByCidIn(@Param("cid")String cid);

    @Query("SELECT h FROM Experience h WHERE REPLACE(h.name, ' ', '') LIKE %:keyword%")
    List<Experience> findByNameContaining(@Param("keyword") String key);
}
