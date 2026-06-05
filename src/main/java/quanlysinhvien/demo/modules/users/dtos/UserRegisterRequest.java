package quanlysinhvien.demo.modules.users.dtos;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;
}
