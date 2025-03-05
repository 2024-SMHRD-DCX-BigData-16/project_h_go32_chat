package com.jtsr.demo.repository;

import com.jtsr.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);
    boolean existsByUserEmail(String email);
    User findByUserUuid(String userUuid);  // ✅ 이메일 없이 UUID만으로 조회
    
}
