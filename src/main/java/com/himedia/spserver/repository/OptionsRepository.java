package com.himedia.spserver.repository;

import com.himedia.spserver.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface OptionsRepository extends JpaRepository<Options, Integer> {

    Options findTop1ByHidOrderByPrice1Asc(int hid);


    List<Options> findAllByHid(int hid);


    Options findByOpid(int opid);


    Options findFirstByHid(int hotelId);

    Options findHidByOpid(Integer pid);

    @Query(" select COALESCE(sum(od.count), 0) from Orders od where od.category = '숙소' and od.opid = :opid and od.checkInDate < :newCheckOut and od.checkOutDate > :newCheckIn")
    int countRoom(@Param("opid") int opid, @Param("newCheckIn") Timestamp newCheckIn, @Param("newCheckOut") Timestamp newCheckOut);

    List<Options> findByHid(int hid);

    Options findTopByOrderByOpidDesc();

    Options findPrice1ByOpid(int opid);
}

