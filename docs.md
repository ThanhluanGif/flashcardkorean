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