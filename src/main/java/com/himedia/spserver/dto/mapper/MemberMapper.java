package com.himedia.spserver.dto.mapper;

import com.himedia.spserver.dto.request.MemberJoinRequest;
import com.himedia.spserver.dto.response.AdminMemberDto;
import com.himedia.spserver.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class MemberMapper {
    public Member toEntity(MemberJoinRequest joinRequest) {
        Member member = new Member();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setUserid(joinRequest.getUserid());
        member.setPwd(passwordEncoder.encode(joinRequest.getPwd()));
        member.setName(joinRequest.getName());
        member.setZipCode(joinRequest.getZipCode());
        member.setAddress(joinRequest.getAddress());
        member.setAddressDetail(joinRequest.getAddressDetail());
        member.setPhone(joinRequest.getPhone());
        member.setBirth(joinRequest.getBirth());
        member.setEmail(joinRequest.getEmail());
        return member;
    }

    public AdminMemberDto toAdminDto(Member member) {
        if(member == null) return null;
        return new AdminMemberDto(
                member.getMid(),
                member.getUserid(),
                member.getName(),
                member.getAddress(),
                member.getPhone(),
                member.getBirth(),
                member.getEmail(),
                member.getIndate(),
                member.getSnsType(),
                member.getRole()
        );
    }

    public List<AdminMemberDto> toAdminDtoList(List<Member> members) {
        return members.stream()
                .map(this::toAdminDto)
                .toList();
    }


    public Page<AdminMemberDto> toAdminDtoPage(Page<Member> memberList) {
        return memberList.map(this::toAdminDto);
    }
}
