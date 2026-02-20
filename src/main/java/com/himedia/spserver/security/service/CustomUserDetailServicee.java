package com.himedia.spserver.security.service;


import com.himedia.spserver.dto.request.MemberDTO;
import com.himedia.spserver.entity.Member;
import com.himedia.spserver.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailServicee implements UserDetailsService {

    final MemberRepository mr;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername - username: " + username + "---------------------------");

        Member member = mr.findByUserid(username);
        System.out.println("member: " + member);
        if( member==null ){
            throw new UsernameNotFoundException(username + "username not found");
        }
        MemberDTO memberDTO = new MemberDTO(
                member.getMid(),
                member.getUserid(),
                member.getPwd(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getZipCode(),
                member.getAddress(),
                member.getAddressDetail(),
                member.getRole()
        );

        System.out.println("memberDTO: " + memberDTO);
        System.out.println("member: " + member);

        return memberDTO;
        // UsernamePasswordAuthenticationToken 로 리턴
        // 로그인에 성공하면 APILoginSuccessHandler
        // 로그인에 실패하면 APILoginFailHandler
        // 로 이동합니다
    }

}
