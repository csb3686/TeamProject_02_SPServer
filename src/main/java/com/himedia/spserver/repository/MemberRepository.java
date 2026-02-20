package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    boolean existsByUserid(String userid);

    boolean existsByNameAndEmail(String name, String email);

    boolean existsByNameAndEmailAndUserid(String name, String email, String userid);

    Member findByEmail(String email);

    Member findByUserid(String username);

    Member findByMid(Integer mid);

    @Query(
            value = "SELECT * FROM member " +
                    "WHERE REPLACE(userid, ' ', '') LIKE CONCAT('%', :key1, '%') " +
                    "OR REPLACE(name, ' ', '') LIKE CONCAT('%', :key2, '%')",
            nativeQuery = true
    )
    List<Member> findByUseridOrNameContaining(@Param("key1") String key1, @Param("key2") String key2);
}
