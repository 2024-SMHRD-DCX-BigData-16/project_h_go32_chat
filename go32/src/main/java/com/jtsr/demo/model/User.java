package com.jtsr.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "user_pw", length = 96, nullable = false)
    private String userPw;

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;

    @Column(name = "user_email", length = 100, unique = true, nullable = false)
    private String userEmail;

    @Column(name = "user_uuid", length = 50, unique = true, nullable = false)
    private String userUuid = UUID.randomUUID().toString(); // 자동 생성

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "joined_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String joinedAt;
    
    public enum Gender {
        M, F
    }
}
