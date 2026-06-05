package quanlysinhvien.demo.modules.users.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quanlysinhvien.demo.config.JwtService;
import quanlysinhvien.demo.modules.users.dtos.AuthenticationResponse;
import quanlysinhvien.demo.modules.users.dtos.UserLoginRequest;
import quanlysinhvien.demo.modules.users.dtos.UserRegisterRequest;
import quanlysinhvien.demo.modules.users.dtos.UserResponse;
import quanlysinhvien.demo.modules.users.entities.User;
import quanlysinhvien.demo.modules.users.repositories.UserRepository;
import quanlysinhvien.demo.modules.users.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
                .password(passwordEncoder.encode(request.getPassword())) // Đã mã hóa
                .fullName(request.getFullName())
                .role(User.Role.USER) // Mặc định role là USER
                .build();

        // Lưu vào DB
        User savedUser = userRepository.save(user);

        // Trả về Response
        return mapToResponse(savedUser);
    }

    @Override
    public AuthenticationResponse authenticateUser(UserLoginRequest request) {
        // Xác thực qua Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        // Nếu xác thực thành công, tìm user và tạo JWT token
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        var userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
        
        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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

