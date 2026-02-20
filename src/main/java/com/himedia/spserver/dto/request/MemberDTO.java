package com.himedia.spserver.dto.request;

import com.himedia.spserver.entity.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

public class MemberDTO extends User {

    public MemberDTO( // 생성자
                      int mid,
                      String username,
                      String password,
                      String name,
                      String email,
                      String phone,
                      String zipCode,
                      String address,
                      String addressDetail,
                      Role role
    ) {
        // 부모클래스 생성자 호출
        super(
                username,
                password,
                Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role.name())
                )
        );

        // 멤버변수들 값 대입
        this.mid = mid;
        this.userid = username;
        this.pwd = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.birth = birth;
        this.role = role;

    }

    private int mid;
    private String userid;
    private String pwd;
    private String name;
    private String email;
    private String phone;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String birth;
    private Role role;


    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("mid", mid);
        dataMap.put("userid", userid);
        dataMap.put("pwd",pwd);
        dataMap.put("name",name);
        dataMap.put("email", email);
        dataMap.put("phone", phone);
        dataMap.put("zipCode", zipCode);
        dataMap.put("address", address);
        dataMap.put("addressDetail", addressDetail);
        dataMap.put("birth", birth);
        dataMap.put("role", role.name());
        return dataMap;
    }

    public Role getRole() {
        return role;
    }
}
