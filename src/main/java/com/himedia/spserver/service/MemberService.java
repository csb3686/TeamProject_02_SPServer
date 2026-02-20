package com.himedia.spserver.service;

import com.himedia.spserver.dto.mapper.MemberMapper;
import com.himedia.spserver.dto.request.MemberFindIdRequest;
import com.himedia.spserver.dto.request.MemberFindPasswordRequest;
import com.himedia.spserver.dto.request.MemberJoinRequest;
import com.himedia.spserver.dto.request.MemberUpdateRequest;
import com.himedia.spserver.entity.Member;
import com.himedia.spserver.entity.Review;
import com.himedia.spserver.repository.MemberRepository;
import com.himedia.spserver.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository mr;
    private final MemberMapper mm;
    private final ReviewRepository rr;

    public boolean hasUserid(String userid) {
        return mr.existsByUserid(userid);
    }

    public void insertMember(MemberJoinRequest request) {
        Member member = mm.toEntity(request);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPwd(passwordEncoder.encode(request.getPwd()));
        mr.save(member);
    }

    public boolean isValidUser(MemberFindIdRequest request) {
        return mr.existsByNameAndEmail(request.getName(), request.getEmail());
    }

    public boolean isValidUser(MemberFindPasswordRequest request) {
        return mr.existsByNameAndEmail(request.getName(), request.getEmail());
    }

    public String getUseridByEmail(String email) {
        Member member = mr.findByEmail(email);
        return member.getUserid();
    }

    public Member getMember(String userid) {
        return mr.findByUserid(userid);
    }

    public void insertKakaoMember(Member member) {
        mr.save(member);
    }

    public void updatePwd(String userid, String pwd) {
        Member member = mr.findByUserid(userid);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPwd(passwordEncoder.encode(pwd));
    }

    public Page<Review> getReviewsByUser(String userid, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("indate").descending());
        return rr.findByUserid(userid, pageable);
    }

    public void updateMember(MemberUpdateRequest request) {
        Member member = mr.findByUserid(request.getUserid());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPwd(passwordEncoder.encode(request.getPwd()));
        member.setName(request.getName());
        member.setZipCode(request.getZipCode());
        member.setAddress(request.getAddress());
        member.setAddressDetail(request.getAddressDetail());
        member.setPhone(request.getPhone());
        member.setBirth(request.getBirth());
        member.setEmail(request.getEmail());
    }

    public HashMap<String, Object> getBirth(String userid) {
        Member member = mr.findByUserid(userid);
        HashMap<String, Object> map = new HashMap<>();
        map.put("birth", member.getBirth());
        map.put("snsType", member.getSnsType());
        return map;
    }
}
