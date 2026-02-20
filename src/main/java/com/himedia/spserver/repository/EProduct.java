package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Experience;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EProduct extends JpaRepository<Experience, Integer> {

    List<Experience> findByNameContainingIgnoreCase(String name);
    Page<Experience> findByNameContainingIgnoreCase(String name, Pageable pageable);

}

