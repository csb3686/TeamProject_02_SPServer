package com.himedia.spserver.service;

import com.himedia.spserver.dto.response.TransDetailDto;
import com.himedia.spserver.entity.Timetable;
import com.himedia.spserver.entity.TransDetail;
import com.himedia.spserver.repository.TimetableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository ttr;

    public Timetable getTimeById(int ttimeid) {
        return ttr.findById(ttimeid).orElseThrow(() -> new RuntimeException("시간표 없음"));
    }


}
