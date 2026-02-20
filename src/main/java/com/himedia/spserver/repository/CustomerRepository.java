package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Qna, Integer> {
    Qna findByQid(int qid);
}
