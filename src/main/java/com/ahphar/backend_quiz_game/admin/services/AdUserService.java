package com.ahphar.backend_quiz_game.admin.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.admin.DTOs.UserRespDTO;
import com.ahphar.backend_quiz_game.admin.mapper.AdUserMapper;
import com.ahphar.backend_quiz_game.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdUserService {
    
    private final UserRepository userRepository;
    private final AdUserMapper userMapper;

    public List<UserRespDTO> getAllUsers(){
        return userRepository.findAllByOrderByRegisteredTimeDesc()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
