package com.backend.services;

import com.backend.DTOS.UserProfileDto;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("User not found with ID=" + id); // log temporar
                    return new RuntimeException("User not found");
                });
    }

    public User updateUserProfile(Long id, UserProfileDto dto) {
        User user = getUserById(id);
        if (dto.getFullName() != null)    user.setFullName(dto.getFullName());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getCity() != null)        user.setCity(dto.getCity());
        if (dto.getAddress() != null)     user.setAddress(dto.getAddress());
        if (dto.getProfileImageUrl() != null) user.setProfileImageUrl(dto.getProfileImageUrl());
        return userRepository.save(user);
    }



}

