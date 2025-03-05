package com.jtsr.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPreferences {

    @Id
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;  // ✅ FK지만 User 객체가 아닌 String으로 유지

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_purpose", nullable = false)
    private TravelPurpose travelPurpose;

    @Column(name = "preferred_mood", length = 50)
    private String preferredMood;

    @Column(name = "preferred_food", length = 50)
    private String preferredFood;

    @Column(name = "last_updated", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastUpdated;

    public enum TravelPurpose {
        LEISURE, BUSINESS, ADVENTURE, RELAXATION
    }
}
