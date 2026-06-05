package quanlysinhvien.demo.modules.users.dtos;

import lombok.Builder;
import lombok.Data;
import quanlysinhvien.demo.modules.users.entities.User.Role;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private LocalDateTime createdAt;
}
