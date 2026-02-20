package com.himedia.spserver.security.filter;

import com.google.gson.Gson;
import com.himedia.spserver.dto.request.MemberDTO;
import com.himedia.spserver.entity.Member;
import com.himedia.spserver.entity.Role;
import com.himedia.spserver.repository.MemberRepository;
import com.himedia.spserver.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JTCheckFilter extends OncePerRequestFilter {
    private final MemberRepository mr;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 전송된 요청(request) 에 담긴 header 로 사용자 정보와 토큰을 점검
        String authHeaderStr = request.getHeader("Authorization");

        if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token is not exist");
        }
        String accessToken = authHeaderStr.substring(7);
        // axios.post(`url`, null, {params:{}, header:{'Authorization';'Bearer ${accessToken}'})

        try{
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);
            System.out.println("JWTUtil claims" + claims);

            int mid = ((Number)claims.get("mid")).intValue();

            Member member = mr.findByMid(mid);
            if (member == null) {
                throw new BadCredentialsException("User not found");
            }

            String userid=(String)claims.get("userid");
            String name = (String) claims.get("name");
            String email = (String) claims.get("email");
            String phone = (String) claims.get("phone");
            String zipCode = (String) claims.get("zipCode");
            String address = (String) claims.get("address");
            String addressDetail = (String) claims.get("addressDetail");
            String birth = (String) claims.get("birth");
            String roleStr = (String) claims.get("role");
            Role role = Role.valueOf(roleStr);

            MemberDTO memberDTO = new MemberDTO(mid, userid, "dummy", name, email, phone, zipCode, address, addressDetail, role);

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDTO, null, memberDTO.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("=== JTCheckFilter Authentication ===");
            System.out.println("Principal: " + auth.getPrincipal());
            System.out.println("Authorities: " + auth.getAuthorities());

            filterChain.doFilter(request, response);

        }catch(Exception e){
            System.out.println("JWT Check Error --------------------");
            System.out.println(e.getMessage());
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println(msg);
            out.close();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        System.out.println("check url : "+ path);

        /*  해당 부분은 삭제, 수정하지 말것 ----------------------------------------------------------------------------------시작*/


        if(request.getMethod().equals("OPTIONS"))
            return true;

        if(path.startsWith("/question"))
            return true;

        if(path.startsWith("/member/login"))  // -> security formLogin의 loginPage 로 이동
            return true;

        if(path.startsWith("/main/getHotelList"))
            return true;

        if(path.startsWith("/main/getTransList"))
            return true;

        if(path.startsWith("/main/getExperience"))
            return true;

        if(path.startsWith("/member/sendMailForUserid"))
            return true;

        if(path.startsWith("/member/findUserid"))
            return true;

        if(path.startsWith("/member/sendMailForPassword"))
            return true;

        if(path.startsWith("/member/getResetToken"))
            return true;

        if(path.startsWith("/member/checkResetToken"))
            return true;

        if(path.startsWith("/member/findPassword"))
            return true;

        if(path.startsWith("/member/updatePwd"))
            return true;

        if(path.startsWith("/member/idCheck"))
            return true;

        if(path.startsWith("/member/join"))
            return true;

        if(path.startsWith("/member/kakaoStart"))
            return true;

        if(path.startsWith("/member/refresh"))
            return true;

        if(path.startsWith("/member/kakaoLogin"))
            return true;

        if(path.startsWith("/favicon.ico"))
            return true;

        if(path.startsWith("/product/getHotelList"))
            return true;

        if(path.startsWith("/product/getProduct"))
            return true;

        if(path.startsWith("/product/getCityList"))
            return true;

        if(path.startsWith("/product/getTransList"))
            return true;

        if(path.startsWith("/product/getExperienceList"))
            return true;

        if(path.startsWith("/product/getHReviewList"))
            return true;

        if(path.startsWith("/product/getBestHReviewList"))
            return true;

        if(path.startsWith("/product/getTReviewList"))
            return true;

        if(path.startsWith("/product/TopCityProducts"))
            return true;

        if(path.startsWith("/hotel/getHotelDetail"))
            return true;

        if(path.startsWith("/hotel/getOptionList"))
            return true;

        if(path.startsWith("/product/getBestTReviewList"))
            return true;

        /*  해당 부분은 삭제, 수정하지 말것 ---------------------------------------------------------------------------------- 종료*/

        if(path.startsWith("/hotel/countRoom"))
            return true;

        if(path.startsWith("/trans/getcount"))
            return true;

        if(path.startsWith("/product/increaseExView"))
            return true;

        if(path.startsWith("/product/increaseHotelView"))
            return true;

        if(path.startsWith("/trans/getTransReviews"))
            return true;

        if(path.startsWith("/trans/getTransDetail"))
            return true;

        if(path.startsWith("/product/getEReviewList"))
            return true;

        if(path.startsWith("/product/getBestEReviewList"))

            return true;

        if(path.startsWith("/product/getExOptionList"))
            return true;

        if(path.startsWith("/product/experienceDetail"))
            return true;

        if(path.startsWith("/customer/getNoticeList"))
            return true;

        if(path.startsWith("/customer/getNoticeDetail"))
            return true;

        if(path.startsWith("/customer/getQnaDetail"))
            return true;

        if(path.startsWith("/customer/getQnaList"))
            return true;

        if(path.startsWith("/product/categoryList"))
            return true;

        if(path.startsWith("/member/findId"))
            return true;

        if(path.startsWith("/member/sendMail"))
            return true;

        if(path.startsWith("/member/confirmcode"))
            return true;

        if(path.startsWith("/member/resetPw"))
            return true;

        if(path.startsWith("/package/getCid"))
            return true;

        if(path.startsWith("/package/getTransList"))
            return true;

        if(path.startsWith("/package/getHotelListByCid"))
            return true;

        if(path.startsWith("/package/getOptionListByHid"))
            return true;

        if(path.startsWith("/package/getExListByCid"))
            return true;

        if(path.startsWith("/package/getHotelByHid"))
            return true;

        if(path.startsWith("/package/getOptionByOpid"))
            return true;

        if(path.startsWith("/package/insertExlistToCartPackage"))
            return true;

        if(path.startsWith("/admin/getEProduct"))
            return true;

        if(path.startsWith("/admin/getHProduct"))
            return true;

        if(path.startsWith("/admin/getTProduct"))
            return true;

        if(path.startsWith("/admin/AdminEProduct"))
            return true;

        if(path.startsWith("/admin/AdminHProduct"))
            return true;

        if(path.startsWith("/admin/AdminTProduct"))
            return true;

        if(path.startsWith("/admin/updateAdminEProduct"))
            return true;

        if(path.startsWith("/admin/updateAdminHProduct"))
            return true;

        if(path.startsWith("/admin/updateAdminTProduct"))
            return true;

        if(path.startsWith("/admin/updateAdminHOption"))
            return true;

        if(path.startsWith("/admin/fileUpload"))
            return true;

        if(path.startsWith("/product/getEReviewList"))
            return true;

        if(path.startsWith("/product/getBestEReviewList"))
            return true;

        if (path.startsWith("/admin/getCities")) {
            return true;
        }

        if (path.startsWith("/product/experienceDetail")) {
            return true;
        }


        return false;
    }
}
