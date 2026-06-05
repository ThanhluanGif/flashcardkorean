# 🇰🇷 FlashcardKorean - Project Architecture & Database Design

Dự án này xây dựng hệ thống Backend cho website học từ vựng tiếng Hàn bằng phương pháp Flashcard. Mã nguồn tuân thủ nghiêm ngặt cấu trúc **Modular/Package by Feature** kết hợp với các quy tắc chuẩn của Spring Boot để đảm bảo tính mở rộng và dễ bảo trì.

---

## 📁 Cấu trúc thư mục (Folder Structure)

```text
src/main/java/quanlysinhvien/demo/
├── DemoApplication.java             # Entry point của ứng dụng
│
├── core/                            # Chứa các class/interface dùng chung (Base)
│   ├── controllers/
│   │   └── BaseController.java
│   ├── services/
│   │   └── BaseService.java
│   └── repositories/
│       └── BaseRepository.java
│
├── config/                          # Các lớp cấu hình (Security, CORS, Beans...)
├── helpers/                         # Các lớp tiện ích (Utilities, Validators...)
│
└── modules/                         # Phân chia theo tính năng (Module)
    ├── users/                       # Module 1: Quản lý người dùng
    │   ├── controllers/             # REST Controllers (Đăng ký, đăng nhập)
    │   ├── dtos/                    # Request/Response DTOs
    │   ├── entities/                # JPA Entities (Bảng users)
    │   ├── repositories/            # UserRepository
    │   └── services/                # Logic nghiệp vụ User
    │       ├── UserService.java
    │       └── impl/ UserServiceImpl.java
    │
    ├── decks/                       # Module 2: Quản lý Bộ thẻ (Ví dụ: TOPIK I, TOPIK II)
    │   ├── controllers/             # DeckController
    │   ├── dtos/                    # DeckRequest / DeckResponse
    │   ├── entities/                # JPA Entities (Bảng decks)
    │   ├── repositories/            # DeckRepository
    │   └── services/                # Logic nghiệp vụ Bộ thẻ
    │       ├── DeckService.java
    │       └── impl/ DeckServiceImpl.java
    │
    └── cards/                       # Module 3: Quản lý Thẻ từ vựng chi tiết
        ├── controllers/             # CardController
        ├── dtos/                    # CardRequest / CardResponse
        ├── entities/                # JPA Entities (Bảng cards)
        ├── repositories/            # CardRepository
        └── services/                # Logic lật thẻ, tính trạng thái nhớ từ
            ├── CardService.java
            └── impl/ CardServiceImpl.java

---

## 🚀 Lộ trình Phát triển (Roadmap)

Dưới đây là các bước tiếp theo để biến cấu trúc thư mục này thành một website Flashcard hoàn chỉnh:

### 📍 Giai đoạn 1: Khởi tạo và Cấu hình cốt lõi
1. **Thiết lập Dự án (Build Tool):**
   - Khởi tạo file `pom.xml` (Maven) hoặc `build.gradle` (Gradle) tại thư mục gốc.
   - Khai báo các Dependencies cơ bản: *Spring Web*, *Spring Data JPA*, *Lombok*, *Database Driver* (MySQL/PostgreSQL), *Spring Security*.
2. **Cấu hình Môi trường (Resources):**
   - Tạo thư mục `src/main/resources`.
   - Thiết lập cấu hình ứng dụng trong `application.yml` (Server port, Database connection, Hibernate, JWT...).

### 📍 Giai đoạn 2: Lập trình Backend (API)
Thực hiện code theo luồng từ dưới lên trên (Bottom-up) cho từng module (`users`, `decks`, `cards`):
1. **Entities:** Định nghĩa các class Map với bảng trong Database (dùng các annotation `@Entity`, `@Table`, quan hệ `@OneToMany`, `@ManyToOne`).
2. **Repositories:** Tạo interface mở rộng từ `JpaRepository` để thao tác với DB.
3. **Services:** Triển khai logic nghiệp vụ (Ví dụ: Thuật toán Spaced Repetition (Lặp lại ngắt quãng) cho Flashcard).
4. **Controllers & DTOs:** Xây dựng RESTful API nhận request và trả về response chuẩn JSON.
5. **Bảo mật (Security):** Tích hợp Spring Security, mã hóa mật khẩu, và bảo vệ API bằng JWT (JSON Web Token).

### 📍 Giai đoạn 3: Tích hợp Giao diện Frontend
Website cần giao diện để người dùng tương tác, có thể chọn một trong hai hướng:
- **Tách rời (SPA - Lựa chọn khuyến nghị):** Xây dựng Frontend độc lập bằng **React**, **Vue**, hoặc **Angular**. Frontend sẽ gọi các API RESTful của Backend thông qua HTTP requests.
- **Render phía Server (SSR):** Sử dụng **Thymeleaf** kết hợp với HTML/CSS/Bootstrap/Tailwind, được xử lý trực tiếp bên trong Spring Boot project.

### 📍 Giai đoạn 4: Kiểm thử và Triển khai
1. Viết Unit Test và Integration Test với `JUnit` và `Mockito`.
2. Tích hợp Swagger (OpenAPI) để document các API endpoints.
3. Đóng gói ứng dụng thành `.jar` và triển khai lên server (AWS, Heroku, Render...) cùng với Database.

---

## 🔍 Đánh giá Hệ thống Backend Hiện tại (Kết thúc Giai đoạn 2.4)

Sau khi hoàn thành các phần **Entities, Repositories, Services và Controllers**, đây là đánh giá tổng quan về hiện trạng của hệ thống. Những ghi chép này dùng làm định hướng để hoàn thiện dự án trong các giai đoạn (Phase 2.5+) sắp tới:

### ✅ Ưu điểm (Strengths)
1. **Kiến trúc mạch lạc (Modular Monolith):** Áp dụng cấu trúc `Package by Feature` giúp code dễ bảo trì. Các modules `users`, `decks`, `cards` hoàn toàn độc lập, rất dễ để mở rộng sau này (ví dụ: chia thành Microservices).
2. **Luồng dữ liệu chuẩn 3 lớp:** Separation of Concerns được thực hiện tốt qua `Controller (HTTP)` -> `Service (Business Logic)` -> `Repository (Database)`.
3. **Quản lý Dữ liệu linh hoạt:** Sử dụng DTO (Data Transfer Object) để tách biệt giữa Entity (Database) và dữ liệu trả về cho Client, giúp ẩn đi các trường nhạy cảm (như mật khẩu).
4. **Logic nghiệp vụ lõi đã hình thành:** Thuật toán **Spaced Repetition System (SRS - Lặp lại ngắt quãng)** đã được cài đặt thành công ở `CardService.reviewCard`, cho phép tự động tính toán thời gian ôn tập tiếp theo dựa trên trí nhớ của người dùng.

### ⚠️ Hạn chế & Vấn đề Cần Khắc phục (Limitations)
1. **Lỗ hổng Bảo mật Nghiêm trọng (Security Flaws):**
   - Mật khẩu người dùng đang được lưu dạng *Plain Text* (chưa băm bằng BCrypt).
   - Chưa tích hợp hệ thống xác thực (Authentication). Bất kỳ ai cũng có thể gọi API nếu biết URL.
   - Lỗi **IDOR** (Insecure Direct Object Reference): API tạo Deck đang truyền `userId` trực tiếp qua URL. Người dùng A có thể truyền `userId` của B để thao tác dữ liệu.
2. **Thiếu Kiểm tra Dữ liệu Đầu vào (Validation):**
   - Chưa chặn được các trường hợp truyền Email sai định dạng, hay Title bị bỏ trống từ Frontend gửi xuống. 
3. **Xử lý Lỗi Cục bộ (Error Handling):**
   - Hiện tại đang sử dụng `try-catch` lặp lại rất nhiều trong các Controller, và trả về lỗi thô (String). 
   - Thiếu một **Global Exception Handler** (`@ControllerAdvice`) để chuẩn hóa Response lỗi thống nhất trên toàn hệ thống.
4. **Vấn đề Hiệu suất khi Dữ liệu Lớn:**
   - Các API `getAll` đang trả về toàn bộ dữ liệu. Cần bổ sung cơ chế **Phân trang và Sắp xếp (Pagination & Sorting)** cho các bảng có nhiều bản ghi (như `Cards`).
5. **Thiếu API Cập nhật (Update):** Hệ thống hiện có Tạo (Create), Đọc (Read), Xóa (Delete) nhưng còn thiếu các chức năng Chỉnh sửa (Update) cho Bộ thẻ và Thẻ.

### 🎯 Kế hoạch Cải thiện (Cho Giai đoạn 2.5)
Để khắc phục những hạn chế trên, các bước phát triển Backend tiếp theo cần tập trung vào:
- **Phase 2.5.1 (Security):** Tích hợp `Spring Security` + `JWT`. Chuyển sang lấy `userId` tự động từ Token thay vì client truyền vào.
- **Phase 2.5.2 (Validation):** Thêm thư viện `spring-boot-starter-validation`, sử dụng các Annotation (`@NotBlank`, `@Email`) vào DTOs.
- **Phase 2.5.3 (Exception Handling):** Tạo `GlobalExceptionHandler` để bọc các Exception thành `ApiResponse` chuẩn (VD: `{ "status": 400, "message": "Email đã tồn tại" }`).
- **Phase 2.5.4 (Features):** Cập nhật thêm các API `PUT/PATCH` để chỉnh sửa Decks/Cards và cấu hình `Pageable` cho Repositories.