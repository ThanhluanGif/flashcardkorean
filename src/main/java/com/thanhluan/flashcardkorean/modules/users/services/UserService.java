package com.thanhluan.flashcardkorean.modules.users.services;

import com.thanhluan.flashcardkorean.modules.users.dtos.AuthenticationResponse;
import com.thanhluan.flashcardkorean.modules.users.dtos.UserLoginRequest;
import com.thanhluan.flashcardkorean.modules.users.dtos.UserRegisterRequest;
import com.thanhluan.flashcardkorean.modules.users.dtos.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest request);
    AuthenticationResponse authenticateUser(UserLoginRequest request);
    List<UserResponse> getAllUsers();
}

