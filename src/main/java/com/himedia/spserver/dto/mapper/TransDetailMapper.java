package com.himedia.spserver.dto.mapper;

import com.himedia.spserver.dto.response.ReviewDto;
import com.himedia.spserver.dto.response.TransDetailDto;
import com.himedia.spserver.entity.Review;
import com.himedia.spserver.entity.TransDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransDetailMapper {

    public TransDetailDto toTranDetailDto(TransDetail transData) {
        TransDetailDto resDto = new TransDetailDto();
        resDto.setCategory(transData.getCategory());
        resDto.setCompany(transData.getCompany());
        resDto.setName(transData.getName());
        resDto.setPrice1(transData.getPrice1());
        resDto.setPrice2(transData.getPrice2());
        resDto.setStarttime(transData.getStarttime());
        resDto.setEndtime(transData.getEndtime());
        resDto.setStart(transData.getStart());
        resDto.setEnd(transData.getEnd());
        resDto.setImage(transData.getImage());
        resDto.setMaxcount(transData.getMaxcount());

        return resDto;
    }
}

