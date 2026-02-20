package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Experience;
import com.himedia.spserver.entity.TransDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TProduct extends JpaRepository<TransDetail, Integer> {

    List<TransDetail> findByNameContainingIgnoreCase(String name);
    Page<TransDetail> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
