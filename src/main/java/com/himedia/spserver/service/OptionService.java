package com.himedia.spserver.service;

import com.himedia.spserver.entity.Options;
import com.himedia.spserver.repository.OptionsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OptionService {

    private final OptionsRepository opr;

    public List<Options> findAllByHid(int hid) {
        return opr.findAllByHid(hid);
    }

    public Options findByOpid(int opid) {
        return opr.findByOpid(opid);
    }

}
