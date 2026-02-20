package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Packagelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PackageListRepository extends JpaRepository<Packagelist, Integer> {

    @Query("SELECT MAX(p.packageid) FROM Packagelist p WHERE p.mid = :mid")
    int findMaxIdByMid(int mid);
}
