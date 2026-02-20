package com.himedia.spserver.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResetTokenService {
    private final Map<String, resetToken> resetTokenStorage = new ConcurrentHashMap<>();
    private static final SecureRandom secureRandom = new SecureRandom();

    @Data
    @AllArgsConstructor
    private static class resetToken {
        private String userid;
        private long createdAt;
    }

    @Scheduled(fixedRate = 60 * 1000) // 미사용 토큰이 누적되지 않도록 1분마다 만료된 토큰 제거
    public void cleanTokens() {
        long now = System.currentTimeMillis();
        resetTokenStorage.entrySet().removeIf(entry ->
                now - entry.getValue().getCreatedAt() > 10 * 60 * 1000 // 만료기간 10분
        );
    }

    public String generateToken(String userid) {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        resetTokenStorage.put(token, new resetToken(userid, System.currentTimeMillis()));
        return token;
    }

    public boolean isValidToken(String token) {
        return resetTokenStorage.containsKey(token);
    }

    public String getUserid(String token) {
        resetToken resetToken = resetTokenStorage.get(token);
        if (resetToken == null) return null;

        synchronized (resetToken) {
            resetTokenStorage.remove(token);
            return resetToken.getUserid();
        }
    }
}
