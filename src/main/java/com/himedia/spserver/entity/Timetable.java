package com.himedia.spserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ttimeid;

    private String timetb; //편성표

}
