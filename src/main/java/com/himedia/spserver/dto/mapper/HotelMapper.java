package com.himedia.spserver.dto.mapper;

import com.himedia.spserver.dto.request.HotelPlusOptionsRequest;
import com.himedia.spserver.dto.request.OptionRequest;
import com.himedia.spserver.entity.Hotel;
import com.himedia.spserver.entity.Options;
import com.himedia.spserver.repository.OptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HotelMapper {

    private final OptionsRepository or;

    public HotelPlusOptionsRequest toMapper(Hotel hotel, int opid) {

        HotelPlusOptionsRequest hdto =  new HotelPlusOptionsRequest();
        hdto.setHid(hotel.getHid());
        hdto.setName(hotel.getName());
        hdto.setImage(hotel.getImage());
        hdto.setContent(hotel.getContent());

        Options options = or.findByOpid(opid);
        OptionRequest optionDTOs = new  OptionRequest();
        optionDTOs.setOpid(opid);
        optionDTOs.setName(options.getName());
        optionDTOs.setContent(options.getContent());
        optionDTOs.setHid(options.getHid());
        optionDTOs.setStartdate(options.getStartdate());
        optionDTOs.setEnddate(options.getEnddate());
        optionDTOs.setPrice1(options.getPrice1());
        optionDTOs.setPrice2(options.getPrice2());
        hdto.setOptions(optionDTOs);

        return hdto;
    }
}
