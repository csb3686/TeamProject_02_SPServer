package com.himedia.spserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class City {

    @Id
    private String cid; //도시ID(법정동코드)

    private String ad1; //지역명 대분류(시, 도)
    private String ad2; //지역명 중분류(군, 구)
    private String ad3; //지역명 소분류(읍, 면, 리, 동 등등)

}
