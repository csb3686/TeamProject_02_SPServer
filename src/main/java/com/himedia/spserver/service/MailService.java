package com.himedia.spserver.service;

import com.himedia.spserver.dto.request.MemberVerifyCodeIdRequest;
import com.himedia.spserver.dto.request.MemberVerifyCodePasswordRequest;
import com.himedia.spserver.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;

@EnableScheduling
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {
    private final MemberRepository mr;
    private final JavaMailSender JMSender;
    private final Map<String, MailCode> codeStorage = new ConcurrentHashMap<>();

    private static final SecureRandom secureRandom = new SecureRandom();

    @Scheduled(fixedRate = 60 * 1000) // 미사용 인증번호가 누적되지 않도록 1분마다 만료된 인증번호 제거
    public void cleanCodes() {
        long now = System.currentTimeMillis();
        codeStorage.entrySet().removeIf(entry ->
                now - entry.getValue().getCreatedAt() > 3 * 60 * 1000
        );
    }

    // 인증번호와 생성시간을 담고 있는 MailCode 하위 클래스를 private static으로 정의
    @Data
    @AllArgsConstructor
    private static class MailCode {
        private int code;
        private long createdAt;
    }

    @Async
    public void sendCode(String email) {
        // SecureRandom을 통한 난수로 100000 ~ 999999 인증번호 생성
        int code = secureRandom.nextInt(900000) + 100000;
        // 인증번호 정보와 생성시간(MailCode 객체)을 이메일이 key인 ConcurrentHashMap에 저장
        codeStorage.put(email, new MailCode(code, System.currentTimeMillis()));
        
        // 메일 작성
        MimeMessage message = JMSender.createMimeMessage();
        try {
            message.setFrom("tourdb@gmail.com");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");

            String body= "<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">"
            + "<tr>"
                + "<td colspan=\"1\" style=\"background-color: #FE9338; height: 30px;\"></td>"
            + "</tr>"
            + "<tr>"
                + "<td style=\"padding: 20px; text-align: center; font-size: 16px; color: #333;\">"
                    + "요청하신 인증번호입니다."
                + "</td>"
            + "</tr>"
            + "<tr>"
                + "<td style=\"padding: 20px; text-align: center; font-size: 32px; font-weight: bold; color: #000;\">"
                    + code
                + "</td>"
            + "</tr>"
            + "<tr>"
                + "<td style=\"padding: 20px; text-align: center; font-size: 16px; color: #555;\">"
                    + "감사합니다."
                + "</td>"
            + "</tr>"
            + "<tr>"
                + "<td style=\"padding: 10px; text-align: right;\">"
                    + "<img src='https://seongheebuc-862.s3.ap-northeast-2.amazonaws.com/logo.png' alt='Logo' width='120' style='display: inline;'>"
                + "</td>"
            + "</tr>"
            + "<tr>"
                + "<td style=\"background-color: #FE9338; height: 30px;\"></td>"
            + "</tr>"

            + "</table>";
            message.setText(body, "UTF-8", "html");
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
        JMSender.send(message);
    }

    // 인증번호 검증 공통 메서드
    private boolean verifyCommon(String email, int inputCode) {
        // codeStorage에 저장된 MailCode 객체 확인
        MailCode storedCode = codeStorage.get(email);
        // 저장된 MailCode 객체가 없으면 false 리턴
        if (storedCode == null) return false;

        // 3분이 경과되면 만료시켜 MailCode 객체를 제거한 후 false 리턴
        if (System.currentTimeMillis() - storedCode.getCreatedAt() > 3 * 60 * 1000) {
            codeStorage.remove(email);
            return false;
        }

        // 인증번호 불일치 시 false 리턴
        if (storedCode.getCode() != inputCode) return false;

        return true;
    }

    // 아이디 / 비밀번호 찾기 분기를 결정하는 메서드
    private boolean verifyWithCondition(String email, int code, BooleanSupplier condition) {

        // 공통 검증 메서드 실행
        if (!verifyCommon(email, code)) return false;

        // 아이디 / 비밀번호 찾기에 맞는 DB 쿼리 조건을 BooleanSupplier 람다식에 담아 getAsBoolean()으로 검증
        if (!condition.getAsBoolean()) return false;

        // 정상 인증 후 저장된 MailCode 객체를 지우고 리턴
        codeStorage.remove(email);
        return true;
    }

    // 인증번호 확인(아이디)
    public boolean verifyCode(MemberVerifyCodeIdRequest request) {
        return verifyWithCondition(
                request.getEmail(),
                request.getInputCode(),
                () -> mr.existsByNameAndEmail(
                        request.getName(),
                        request.getEmail()
                )
        );
    }

    // 인증번호 확인(비밀번호)
    public boolean verifyCode(MemberVerifyCodePasswordRequest request) {
        return verifyWithCondition(
                request.getEmail(),
                request.getInputCode(),
                () -> mr.existsByNameAndEmailAndUserid(
                        request.getName(),
                        request.getEmail(),
                        request.getUserid()
                )
        );
    }
}
