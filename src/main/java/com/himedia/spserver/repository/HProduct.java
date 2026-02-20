package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Experience;
import com.himedia.spserver.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HProduct extends JpaRepository<Hotel, Integer> {


    List<Hotel> findByNameContainingIgnoreCase(String name);
    Page<Hotel> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
