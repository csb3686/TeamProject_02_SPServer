package com.himedia.spserver.controller;

import com.google.gson.Gson;
import com.himedia.spserver.dto.KakaoProfile;
import com.himedia.spserver.dto.OAuthToken;
import com.himedia.spserver.dto.request.*;
import com.himedia.spserver.entity.*;
import com.himedia.spserver.security.util.CustomJWTException;
import com.himedia.spserver.security.util.JWTUtil;
import com.himedia.spserver.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService ms;
    private final MailService mailService;
    private final ResetTokenService resetTokenService;
    private final ReviewService rs;

    @PostMapping("/idCheck")
    public HashMap<String, Object> idCheck(@RequestParam("userid")String userid) {
        HashMap<String, Object> result = new HashMap<>();
        if(!ms.hasUserid(userid)) {
            result.put("msg", "confirmed");
        }else {
            result.put("msg", "failed");
        }
        return result;
    }

    @PostMapping("/join")
    public HashMap<String, Object> join(@Valid @RequestBody MemberJoinRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        ms.insertMember(request);
        result.put("msg", "success");
        return result;
    }

    @PostMapping("/sendMailForUserid")
    public HashMap<String, Object> sendMailForUserid(@Valid @RequestBody MemberFindIdRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        if(ms.isValidUser(request)) {
            mailService.sendCode(request.getEmail());
            result.put("msg", "confirmed");
        }else {
            result.put("msg", "failed");
        }
        return result;
    }

    @PostMapping("/findUserid")
    public HashMap<String, Object> findUserid(@Valid @RequestBody MemberVerifyCodeIdRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        if(mailService.verifyCode(request)) {
            result.put("msg", "confirmed");
            result.put("userid", ms.getUseridByEmail(request.getEmail()));
        }else {
            result.put("msg", "failed");
        }
        return result;
    }

    @PostMapping("/sendMailForPassword")
    public HashMap<String, Object> sendMailForPassword(@Valid @RequestBody MemberFindPasswordRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        if(ms.isValidUser(request)) {
            mailService.sendCode(request.getEmail());
            result.put("msg", "confirmed");
        }else {
            result.put("msg", "failed");
        }
        return result;
    }

    @PostMapping("/findPassword")
    public HashMap<String, Object> findPassword(@Valid @RequestBody MemberVerifyCodePasswordRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        if(mailService.verifyCode(request)) {
            result.put("msg", "confirmed");
        }else {
            result.put("msg", "failed");
        }
        return result;
    }

    @GetMapping("/getResetToken")
    public HashMap<String, Object> getResetToken(@RequestParam("userid") String userid) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("resetToken", resetTokenService.generateToken(userid));
        return result;
    }

    @GetMapping("/checkResetToken")
    public HashMap<String, Object> checkResetToken(@RequestParam("resetToken") String resetToken) {
        HashMap<String, Object> result = new HashMap<>();
        if(resetTokenService.isValidToken(resetToken)) {
            result.put("msg", "confirmed");
        }else {
            result.put("msg", "failed");
        }
        return result;
    }

    @PostMapping("/updatePwd")
    public HashMap<String, Object> updatePwd(@RequestParam("resetToken") String resetToken, @RequestParam("pwd") String pwd) {
        HashMap<String, Object> result = new HashMap<>();
        String userid = resetTokenService.getUserid(resetToken);
        if(userid == null) {
            result.put("msg", "expired");
        }
        else {
            ms.updatePwd(userid, pwd);
            result.put("msg", "success");
        }
        return result;
    }

    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/kakaoStart")
    public @ResponseBody String kakaoStart() {
        String a = "<script type='text/javascript'>"
                + "location.href='https://kauth.kakao.com/oauth/authorize?"
                + "client_id=" + client_id + "&"
                + "redirect_uri=" + redirect_uri + "&"
                + "response_type=code';" + "</script>";
        return a;
    }

    @RequestMapping("/kakaoLogin")
    public void kakaoLogin(HttpServletRequest request, HttpServletResponse response )
            throws IOException {
        String code = request.getParameter("code");
        String endpoint = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(endpoint);
        String bodyData = "grant_type=authorization_code&";
        bodyData += "client_id=" + client_id + "&";
        bodyData += "redirect_uri=" + redirect_uri + "&";
        bodyData += "code=" + code;

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setDoOutput(true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
        bw.write(bodyData);
        bw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String input = "";
        StringBuilder sb = new StringBuilder();
        while ((input = br.readLine()) != null) {
            sb.append(input);
        }
        Gson gson = new Gson();
        OAuthToken oAuthToken = gson.fromJson(sb.toString(), OAuthToken.class);
        String endpoint2 = "https://kapi.kakao.com/v2/user/me";
        URL url2 = new URL(endpoint2);

        HttpsURLConnection conn2 = (HttpsURLConnection) url2.openConnection();
        conn2.setRequestProperty("Authorization", "Bearer " + oAuthToken.getAccess_token());
        conn2.setDoOutput(true);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream(), "UTF-8"));
        String input2 = "";
        StringBuilder sb2 = new StringBuilder();
        while ((input2 = br2.readLine()) != null) {
            sb2.append(input2);
            System.out.println(input2);
        }
        Gson gson2 = new Gson();
        KakaoProfile kakaoProfile = gson2.fromJson(sb2.toString(), KakaoProfile.class);
        KakaoProfile.KakaoAccount ac = kakaoProfile.getAccount();
        KakaoProfile.KakaoAccount.Profile pf = ac.getProfile();
        System.out.println("id : " + kakaoProfile.getId());
        System.out.println("Profile-Nickname : " + pf.getNickname());

        Member member = ms.getMember( kakaoProfile.getId() );
        if( member == null ){
            member = new Member();
            member.setUserid( kakaoProfile.getId() );
            member.setName( pf.getNickname() );
            member.setEmail( kakaoProfile.getId() );
            BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
            member.setPwd(pe.encode("KAKAO"));
            member.setRole(Role.USER);
            member.setSnsType(SnsType.KAKAO);
            ms.insertKakaoMember(member);
        }
        response.sendRedirect("http://43.200.240.59/kakaoIdLogin/"+member.getUserid());
    }

    @GetMapping("/refresh/{refreshToken}")
    public HashMap<String,Object> refresh(@PathVariable("refreshToken") String refreshToken, @RequestHeader("Authorization") String authHeader) throws CustomJWTException {
        HashMap<String,Object> result = new HashMap<>();

        if(refreshToken == null || refreshToken.isEmpty()){
            throw new CustomJWTException("NULL_REFRESH");
        }
        if(authHeader == null || authHeader.length()<7){
            throw new CustomJWTException("INVALID_HEADER");
        }

        String accessToken = authHeader.substring(7);

        // 기한 만료 체크
        boolean expiredResult  = true;
        try{
            JWTUtil.validateToken(accessToken);
        }catch (CustomJWTException e){
            if(e.getMessage().equals("Expired"))
                expiredResult = false;
        }

        if(expiredResult){ // 유효기간 만료 전
            System.out.println("토큰 유효기간 만료전 ----- 계속 사용");
            result.put("accessToken", accessToken);
            result.put("refreshToken", refreshToken);
        }else{ // 유효기간 만료
            System.out.println("토큰 유효기간 만료 후 ---- 토큰 교체");
            // refreshToken 에서 claims 를 추출
            Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
            // 추출한 claims로 accessToken 재발급
            String newAccessToken = JWTUtil.generateToken(claims, 1);

            String newRefreshToken = "";
            int exp = (Integer) claims.get("exp");
            java.util.Date expDate = new java.util.Date( (long)exp * (1000));
            long gap = expDate.getTime() - System.currentTimeMillis();
            long leftMin = gap / (1000*60);

            if(leftMin < 60){
                newRefreshToken = JWTUtil.generateToken(claims, 60*24);
            }else{
                newRefreshToken = refreshToken;
            }
            result.put("accessToken", newAccessToken);
            result.put("refreshToken", newRefreshToken);
        }

        return result;
    }

    @GetMapping("/checkAdmin")
    public HashMap<String, Object> checkAdmin(Authentication auth) {
        HashMap<String, Object> result = new HashMap<>();
        if(auth == null) result.put("isAdmin", false);

        MemberDTO member = (MemberDTO) auth.getPrincipal();
        boolean isAdmin = member.getRole() == Role.ADMIN;
        result.put("isAdmin", isAdmin);
        return result;
    }

    @PostMapping(value = "/insertReview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HashMap<String, Object> insertReview(
            @RequestPart("dto") ReviewInsertRequest dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {

        HashMap<String, Object> result = new HashMap<>();

        if (rs.existsByOid(dto.getOid())) {
            result.put("msg", "fail");
            result.put("error", "이미 리뷰 있음");
            return result;
        }

        Review review = rs.insertReview(dto, imageFile);

        result.put("msg", "ok");
        result.put("reviewId", review.getRid());
        return result;

//        HashMap<String, Object> result = new HashMap<>();
//        try {
//            System.out.println("DTO 받음: " + dto);
//            System.out.println("imageFile: " + imageFile);
//
//            Review review = rs.insertReview(dto, imageFile);
//            result.put("msg", "ok");
//            result.put("reviewId", review.getRid()); // 저장된 리뷰 ID 확인용
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.put("msg", "fail");
//            result.put("error", e.getMessage());
//        }
//        return result;
    }

    @GetMapping("/getMyReviews")
    public Map<String, Object> getMyReviews(@RequestParam String userid,
                                            @RequestParam(defaultValue = "1") int page) {

        Page<Review> reviewPage = ms.getReviewsByUser(userid, page);

        Map<String, Object> result = new HashMap<>();
        result.put("list", reviewPage.getContent());
        result.put("page", page);
        result.put("totalPages", reviewPage.getTotalPages());
        result.put("totalCount", reviewPage.getTotalElements());

        return result;
    }

    @PostMapping("/updateMember")
    public HashMap<String, Object> updateMember(@RequestBody MemberUpdateRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        ms.updateMember(request);
        result.put("msg", "success");
        return result;
    }

    @GetMapping("/getBirth")
    public HashMap<String, Object> getBirth(@RequestParam String userid) {
        return ms.getBirth(userid);
    }

}
