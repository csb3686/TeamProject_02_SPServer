package com.himedia.spserver.dto.request;

import com.himedia.spserver.entity.Experience;
import com.himedia.spserver.entity.Hotel;
import com.himedia.spserver.entity.TransDetail;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminSerchDto {

    private int id;
    private String name;
    private String category;


}
