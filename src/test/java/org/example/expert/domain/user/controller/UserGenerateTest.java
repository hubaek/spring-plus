package org.example.expert.domain.user.controller;

import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;


@SpringBootTest
public class UserGenerateTest {

    @Test
    public void generateUsers() throws IOException {
        FileWriter writer = new FileWriter("users.csv");
        for (int i = 0; i < 1000000; i++) {
            String email = "tuser" + i + "@example.com";
            String nickname = "user-" + UUID.randomUUID();
            String password = "1234";
            UserRole userRole = UserRole.USER;

            writer.write(String.format("%s,%s,%s,%s\n", email, nickname, password, userRole));
        }
        writer.close();
    }
}