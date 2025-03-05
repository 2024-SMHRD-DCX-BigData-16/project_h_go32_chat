package com.jtsr.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()  // 🔹 모든 요청 허용
            )
            .csrf(csrf -> csrf.disable()) // 🔹 CSRF 보호 비활성화 (필요에 따라 설정)
            .formLogin(form -> form.disable()); // 🔹 로그인 화면 비활성화

        return http.build();
    }
}
