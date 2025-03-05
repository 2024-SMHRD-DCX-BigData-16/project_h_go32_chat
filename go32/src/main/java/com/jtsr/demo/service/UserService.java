package com.jtsr.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtsr.demo.model.User;
import com.jtsr.demo.repository.UserRepository;

@Service
public class UserService {

	
	 @Autowired	
	    private UserRepository userRepository;

	 public User getUserByUuid(String userUuid) {
	        return userRepository.findByUserUuid(userUuid);
	    }
	}

