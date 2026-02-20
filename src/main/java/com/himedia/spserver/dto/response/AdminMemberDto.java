package com.himedia.spserver.dto.response;

import com.himedia.spserver.entity.Role;
import com.himedia.spserver.entity.SnsType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdminMemberDto {
    private int mid;

    private String userid;
    private String name;
    private String address;
    private String phone;
    private LocalDate birth;
    private String email;
    private Timestamp indate;
    private SnsType snsType;
    private Role role;
}
