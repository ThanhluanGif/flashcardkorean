package quanlysinhvien.demo.modules.users.services;

import quanlysinhvien.demo.modules.users.dtos.UserRegisterRequest;
import quanlysinhvien.demo.modules.users.dtos.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest request);
    List<UserResponse> getAllUsers();
}

