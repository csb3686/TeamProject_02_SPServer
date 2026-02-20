package com.himedia.spserver.security;

import com.himedia.spserver.security.filter.JTCheckFilter;
import com.himedia.spserver.security.handler.APILoginFailHandler;
import com.himedia.spserver.security.handler.APILoginSuccessHandler;
import com.himedia.spserver.security.handler.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class CustomSecurityConfig {
    private final JTCheckFilter jtCheckFilter;

    @Bean // security가 시작되면 가장 먼저 자동으로 실행되는 메서드
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("------------- securityFilterChain : start -------------");

        // cors
        http.cors(
                httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(  corsConfigurationSource()  );
                }
        );

        // csrf - 토큰사용O & 세션사용 x  -> security csrf 기능은 사용하지 않음
        http.csrf(config-> config.disable());

        http.formLogin(
                config ->{
                    config.loginPage("/member/login");
                    // -> security / service / CustomUserDetailServicee 안의
                    // loadUserByUsername 메서드를 호출
                    config.successHandler( new APILoginSuccessHandler() );
                    config.failureHandler( new APILoginFailHandler() );
                }
        );

        // 토큰 여부에 따른 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth
                // permitAll()로 자유롭게 접근 가능한 페이지
                .requestMatchers(
                        "/question",
                        "/member/login",
                        "/member/idCheck",
                        "/member/join",
                        "/member/sendMailForUserid",
                        "/member/findUserid",
                        "/member/sendMailForPassword",
                        "/member/findPassword",
                        "/member/getResetToken",
                        "/member/checkResetToken",
                        "/member/updatePwd",
                        "/main/getHotelList",
                        "/main/getTransList",
                        "/main/getExperience",
                        "/member/kakaoStart",
                        "/member/kakaoLogin",
                        "/member/refresh",
                        "/product/getHotelList",
                        "/product/getProduct",
                        "/product/getCityList",
                        "/product/getTransList",
                        "/product/getExperienceList",
                        "/product/getHReviewList",
                        "/product/getBestEReviewList",
                        "/product/getBestHReviewList",
                        "/product/getTReviewList",
                        "/product/TopCityProducts",
                        "/product/getTransDetail/**",
                        "/product/increaseHotelView/**",
                        "/product/increaseExView/**",
                        "/hotel/getHotelDetail/**",
                        "/hotel/getOptionList/**",
                        "/hotel/countRoom/**",
                        "/trans/getTransList/**",
                        "/trans/getTransDetail/**",
                        "/trans/getcount/**",
                        "/product/getBestTReviewList",
                        "/package/getCidByKeyword",
                        "/package/getTransList",
                        "/package/getHotelListByCid",
                        "/package/getOptionListByHid",
                        "/package/getHotelByHid/**",
                        "/package/getOptionByOpid/**",
                        "/package/insertExlistToCartPackage",
                        "/package/getExListByCid",
                        "/product/getEReviewList",
                        "/product/getBestEReviewList",
                        "/product/experienceDetail/**"
                ).permitAll()
                // /admin 및 하위 페이지는 전부 Role.ADMIN 만 접근 가능
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 그 외의 모든 페이지는 JWT 토큰 필요
                .anyRequest().authenticated()
        ).exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) ->
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                )
        );

        // 토큰체크 도구(환경) 설정
        http.addFilterAfter(jtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        // 에러처리 - 토큰, 로그인 이외의 예외처리에 대한 설정(예 : 잘못된 양식의 요청(request) 등)
        http.exceptionHandling(config -> {
                config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 아이피
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 메서드
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        // 헤더
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        // 전송해줄 데이터의 JSON 처리
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
