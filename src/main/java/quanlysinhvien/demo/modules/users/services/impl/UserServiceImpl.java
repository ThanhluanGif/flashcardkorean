package quanlysinhvien.demo.modules.users.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quanlysinhvien.demo.modules.users.dtos.UserRegisterRequest;
import quanlysinhvien.demo.modules.users.dtos.UserResponse;
import quanlysinhvien.demo.modules.users.entities.User;
import quanlysinhvien.demo.modules.users.repositories.UserRepository;
import quanlysinhvien.demo.modules.users.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        // Kiểm tra xem username hoặc email đã tồn tại chưa
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Tạo Entity User từ Request
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                // TODO: Password này CẦN phải được mã hóa (BCrypt) khi tích hợp Spring Security
                .password(request.getPassword()) 
                .fullName(request.getFullName())
                .role(User.Role.USER) // Mặc định role là USER
                .build();

        // Lưu vào DB
        User savedUser = userRepository.save(user);

        // Trả về Response
        return mapToResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Hàm tiện ích chuyển đổi Entity -> DTO Response
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

