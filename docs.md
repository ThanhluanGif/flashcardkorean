# 🇰🇷 FlashcardKorean - Project Architecture & Database Design

Dự án này xây dựng hệ thống Backend cho website học từ vựng tiếng Hàn bằng phương pháp Flashcard. Mã nguồn tuân thủ nghiêm ngặt cấu trúc **Modular/Package by Feature** kết hợp với các quy tắc chuẩn của Spring Boot để đảm bảo tính mở rộng và dễ bảo trì.

---

## 📁 Cấu trúc thư mục (Folder Structure)

```text
src/main/java/com/thanhluan/flashcardkorean/
├── FlashcardKoreanApplication.java    # Entry point của ứng dụng
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
```

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
### 📍 Giai đoạn 3: Phát triển Giao diện Frontend (Chi tiết Implementation)
Xây dựng ứng dụng Single Page Application (SPA) hiện đại kết nối với Backend thông qua RESTful API.

#### 🏗️ Phase 3.1: Khởi tạo & Hạ tầng Cốt lõi
*   **Task 1: Setup Project:** Khởi tạo dự án bằng Vite (React + TypeScript). Cấu hình folder structure chuẩn (`src/components`, `src/pages`, `src/services`, `src/store`, `src/hooks`, `src/assets`).
*   **Task 2: API & Security:** Thiết lập Axios Instance. Cài đặt Interceptors để tự động đính kèm `Authorization: Bearer <Token>` vào header và xử lý lỗi 401 (Unauthorized) tập trung.
*   **Task 3: Global State Management:** Sử dụng **Zustand** để quản lý trạng thái đăng nhập (`isAuthenticated`, `user`, `token`) và thông tin cá nhân người dùng.

#### 🔐 Phase 3.2: Xác thực & Bảo mật (Auth UI)
*   **Task 4: Trang Đăng nhập (Login Page):** Giao diện form đăng nhập, xử lý validation và lưu token vào LocalStorage/Zustand.
*   **Task 5: Trang Đăng ký (Register Page):** Giao diện form đăng ký tài khoản mới với các trường: Email, Username, Full Name, Password.
*   **Task 6: Protected Routes:** Xây dựng component bao bọc (Wrapper) để chặn các trang yêu cầu đăng nhập, tự động redirect về `/login` nếu chưa có token.

#### 📁 Phase 3.3: Quản lý Bộ thẻ (Deck Management)
*   **Task 7: Dashboard (Deck List):** Trang chủ sau khi đăng nhập, hiển thị danh sách các bộ thẻ (Decks) dưới dạng Grid/Card UI. 
*   **Task 8: CRUD Deck:** Giao diện Modal/Form để thêm mới, sửa tên/mô tả hoặc xóa bộ thẻ. Hiển thị thống kê số lượng thẻ bên trong mỗi bộ.

#### 🎴 Phase 3.4: Quản lý Thẻ chi tiết (Card Management)
*   **Task 9: Card Explorer:** Trang hiển thị toàn bộ danh sách thẻ của một bộ cụ thể. Hỗ trợ lọc theo trạng thái (New, Learning, Mastered).
*   **Task 10: CRUD Card:** Form tạo mới/chỉnh sửa thẻ với các trường: Mặt trước (Tiếng Hàn), Mặt sau (Nghĩa), Ví dụ (Example).

#### 🧠 Phase 3.5: Hệ thống Ôn tập SRS (Core Feature)
*   **Task 11: Giao diện Flashcard (Study UI):** Thiết kế component Flashcard với hiệu ứng 3D Flip (Lật thẻ) mượt mà bằng CSS. Hiển thị thông tin mặt trước, sau khi Click sẽ lật ra mặt sau và ví dụ.
*   **Task 12: Đánh giá SRS:** Sau khi lật thẻ, hiển thị 4 nút đánh giá mức độ nhớ (0 - Quên, 1 - Khó, 2 - Tốt, 3 - Dễ). Tích hợp gọi API `/api/v1/cards/{id}/review` để Backend tính toán ngày ôn tập tiếp theo.

#### 🎨 Phase 3.6: Giao diện & Trải nghiệm người dùng (UX/UI)
*   **Task 13: Main Layout:** Xây dựng Header (Logo, Profile, Logout) và Sidebar điều hướng.
*   **Task 14: Responsive Design:** Tối ưu hóa giao diện hiển thị tốt trên cả Desktop và Mobile (ưu tiên Mobile vì người dùng thường học từ vựng khi rảnh trên điện thoại).
*   **Task 15: Hiệu ứng & Thông báo:** Tích hợp React-Toastify để hiển thị thông báo thành công/lỗi sinh động.

---

## 🚀 Giai đoạn 4: Kiểm thử và Triển khai
2. Tích hợp Swagger (OpenAPI) để document các API endpoints.
3. Đóng gói ứng dụng thành `.jar` và triển khai lên server (AWS, Heroku, Render...) cùng với Database.

---

## 🔍 Đánh giá Hệ thống Backend Hiện tại (Cập nhật mới nhất)

Sau khi hoàn thành các giai đoạn cốt lõi, đây là đánh giá chi tiết về hiện trạng của hệ thống. Những ghi chép này phản ánh chính xác mã nguồn hiện tại và định hướng cho các bước tối ưu hóa tiếp theo:

### ✅ Ưu điểm (Strengths)
1. **Kiến trúc Modular Monolith chuẩn:** Áp dụng `Package by Feature` kết hợp với các `Base classes` (BaseController, BaseService, BaseRepository) giúp mã nguồn cực kỳ gọn gàng, dễ mở rộng và bảo trì.
2. **Bảo mật toàn diện:**
   - Đã tích hợp **Spring Security + JWT** cho cơ chế xác thực không trạng thái (Stateless).
   - Mật khẩu được mã hóa an toàn bằng **BCrypt**.
   - Khắc phục hoàn toàn lỗi **IDOR** bằng cách lấy `userId` trực tiếp từ Security Context và kiểm tra quyền sở hữu dữ liệu trước khi xử lý.
3. **Cấu hình CORS sẵn sàng:** Đã cấu hình cho phép các domain Frontend phổ biến (`localhost:3000`, `localhost:5173`) kết nối, sẵn sàng cho việc tích hợp đa nền tảng.
4. **Xử lý lỗi tập trung (Global Exception Handling):** Sử dụng `@ControllerAdvice` để chuẩn hóa toàn bộ phản hồi lỗi về định dạng JSON (`ErrorResponse`), giúp Frontend dễ dàng bắt và hiển thị thông báo.
5. **Kiểm soát dữ liệu đầu vào:** Sử dụng **Bean Validation** (@NotBlank, @Email, @Size...) trong các DTOs để đảm bảo tính toàn vẹn của dữ liệu trước khi vào tầng Service.
6. **Tài liệu API tự động:** Đã tích hợp **Swagger/OpenAPI**, giúp việc tra cứu và thử nghiệm API trở nên trực quan và nhanh chóng.
7. **Logic nghiệp vụ SRS:** Thuật toán **Spaced Repetition System** đã được cài đặt, đảm bảo tính năng cốt lõi của một ứng dụng Flashcard.

### ⚠️ Hạn chế & Vấn đề Cần Khắc phục (Limitations)
1. **Thiếu Phân trang & Sắp xếp (Pagination & Sorting):** Các API lấy danh sách (như lấy tất cả thẻ trong một bộ) hiện đang trả về toàn bộ dữ liệu. Cần nâng cấp lên `Pageable` để đảm bảo hiệu suất khi số lượng thẻ lên đến hàng nghìn.
2. **Độ bao phủ Kiểm thử (Testing Coverage):** Số lượng Unit Test và Integration Test còn hạn chế. Cần bổ sung thêm các bản kiểm thử cho tầng Service để đảm bảo logic SRS hoạt động chính xác trong mọi trường hợp.
3. **Tính năng Tìm kiếm & Lọc:** Hệ thống còn thiếu các API hỗ trợ tìm kiếm từ vựng hoặc lọc thẻ theo trạng thái ghi nhớ (Ghi nhớ tốt, cần ôn lại...).
4. **Quản lý Media:** Hiện tại hệ thống chỉ xử lý văn bản. Trong tương lai, việc hỗ trợ hình ảnh và âm thanh cho Flashcard sẽ yêu cầu tích hợp thêm các dịch vụ lưu trữ (như AWS S3 hoặc Cloudinary).
5. **Logging & Monitoring:** Chưa có hệ thống log chi tiết để theo dõi hành vi người dùng hoặc vết lỗi (tracing) khi hệ thống gặp sự cố trên môi trường production.

### 🎯 Kế hoạch Tiếp theo (Optimization Phase)
- **Tối ưu hiệu suất:** Triển khai Phân trang cho các API lấy danh sách.
- **Nâng cao trải nghiệm:** Bổ sung API tìm kiếm và lọc thẻ.
- **Củng cố chất lượng:** Viết thêm các bộ test case cho các luồng nghiệp vụ quan trọng.
- **Mở rộng tính năng:** Nghiên cứu tích hợp Upload ảnh/âm thanh cho thẻ từ vựng.

### 📍 Giai đoạn 2.6: Chuẩn bị trước khi xây dựng Frontend
Trước khi bắt đầu phát triển giao diện (Frontend) kết nối với Backend, cần hoàn thành các bước bắt buộc sau để đảm bảo hệ thống hoạt động trơn tru:

1. **Cấu hình CORS trong Spring Security:** Cho phép Frontend (ví dụ: chạy trên `http://localhost:3000` hoặc `http://localhost:5173`) gọi API của Backend mà không bị trình duyệt chặn bởi lỗi Cross-Origin.
2. **Dọn dẹp lại Controller (Xóa bỏ try-catch dư thừa):** Loại bỏ các khối `try-catch` cục bộ trong Controller. Để cho các Exception được đẩy lên và xử lý tập trung bởi `GlobalExceptionHandler`, đảm bảo định dạng lỗi trả về luôn chuẩn xác (JSON format).
3. **Cài đặt Swagger (OpenAPI Documentation):** Tích hợp `springdoc-openapi` để tự động tạo tài liệu API UI, giúp Frontend Developer dễ dàng tra cứu các endpoint, payload và response.
4. **Test toàn bộ luồng API (Postman / Swagger):** Kiểm thử thủ công các luồng cốt lõi (Đăng ký -> Đăng nhập lấy Token -> Tạo Deck -> Tạo Card -> Ôn tập) để đảm bảo Backend xử lý đúng logic nghiệp vụ trước khi Frontend gọi đến.
5. **(Tùy chọn) Refactor tên Package:** Đổi tên base package từ `quanlysinhvien.demo` thành tên phù hợp với dự án (ví dụ: `com.thanhluan.flashcardkorean`) để code base gọn gàng và chuyên nghiệp hơn.