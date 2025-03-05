package com.jtsr.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    private String userUuid;
    private String gender;
    private String travelPurpose;
    private String preferredMood;
    private String preferredFood;
}
