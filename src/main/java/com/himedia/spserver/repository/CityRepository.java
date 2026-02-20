package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Cart;
import com.himedia.spserver.entity.City;
import com.himedia.spserver.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, String> {
    List<City> findAll();


    List<City> findByCidStartingWith(String s);
    
    

    City findFirstCidByAd1ContainingOrAd2Containing(String keyword, String keyword1);

    List<City> findByAd1ContainingOrAd2ContainingOrAd3Containing(String keyword, String keyword1, String keyword2);

}
