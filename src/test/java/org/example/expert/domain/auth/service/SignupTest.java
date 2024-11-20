package org.example.expert.domain.auth.service;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignupTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Test
    void signup() {

        // Act: 10명의 사용자 등록
        IntStream.rangeClosed(372854, 472853).forEach(i -> {
            SignupRequest request = new SignupRequest();
            request.setEmail("user" + i + "@example.com");
            request.setPassword("password" + i);
            request.setUserRole("USER");
            request.setNickname("nickname" + i);

            SignupResponse response = authService.signup(request);

            // 각 회원가입 응답 검증
            assertNotNull(response, "SignupResponse는 null이 아니어야 합니다.");
            assertNotNull(response.getBearerToken(), "BearerToken은 null이 아니어야 합니다.");
            assertFalse(response.getBearerToken().isEmpty(), "BearerToken은 비어있으면 안 됩니다.");
        });

        // Assert: 데이터베이스에 10명의 사용자가 저장되었는지 확인
        long count = userRepository.count();
        assertEquals(472853, count, "데이터베이스에 정확히 10명의 사용자가 있어야 합니다.");

        // 개별 사용자 검증
        IntStream.rangeClosed(1, 10).forEach(i -> {
            String email = "user" + i + "@example.com";
            User user = userRepository.findByEmail(email).orElse(null);
            assertNotNull(user, "사용자가 존재해야 합니다: " + email);
            assertEquals("nickname" + i, user.getNickname(), "닉네임이 일치해야 합니다.");
            assertEquals("USER", user.getUserRole().name(), "UserRole이 USER여야 합니다.");
            assertTrue(passwordEncoder.matches("password" + i, user.getPassword()),
                    "비밀번호가 올바르게 인코딩되어야 합니다.");
        });

    }
}