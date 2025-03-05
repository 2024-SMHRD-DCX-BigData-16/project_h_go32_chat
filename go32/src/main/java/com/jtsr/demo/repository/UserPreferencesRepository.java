package com.jtsr.demo.repository;

import com.jtsr.demo.model.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, String> {
	
	
}
