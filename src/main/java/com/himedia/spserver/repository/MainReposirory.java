package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainReposirory extends JpaRepository<Experience, Integer> {
    List<Experience> findTop4ByOrderBySalecountDesc();
}
