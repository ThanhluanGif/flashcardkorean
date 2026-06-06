# 🇰🇷 FlashcardKorean - Project Architecture & Database Design

Dự án này xây dựng hệ thống Backend và Frontend cho website học từ vựng tiếng Hàn bằng phương pháp Flashcard. Mã nguồn tuân thủ nghiêm ngặt cấu trúc **Modular/Package by Feature** kết hợp với các quy tắc chuẩn của Spring Boot và React để đảm bảo tính mở rộng và dễ bảo trì.

---

## 📁 Cấu trúc thư mục (Folder Structure)

### Backend (Spring Boot)
```text
src/main/java/com/thanhluan/flashcardkorean/
├── core/                            # Chứa các class/interface dùng chung (Base)
├── config/                          # Các lớp cấu hình (Security, CORS, Swagger...)
├── helpers/                         # Các lớp tiện ích (Global Exception, Error Response)
└── modules/                         # Phân chia theo tính năng (Module)
    ├── users/                       # Quản lý người dùng & Auth
    ├── decks/                       # Quản lý Bộ thẻ
    └── cards/                       # Quản lý Thẻ từ vựng & SRS Logic
```

### Frontend (React + TypeScript)
```text
frontend/src/
├── api/                             # Axios Instance & API Calls
├── components/                      # UI Components dùng chung
├── layouts/                         # MainLayout, AuthLayout
├── pages/                           # Các trang chính (Dashboard, Study, Auth...)
├── store/                           # Quản lý trạng thái (Zustand)
└── types/                           # TypeScript Interfaces
```

---

## 🗄️ Thiết kế Cơ sở dữ liệu (Database Design)

Hệ thống sử dụng cơ sở dữ liệu quan hệ với sơ đồ thực thể chính như sau:

### 1. Bảng `users` (Người dùng)
- `id`: Khóa chính (BigInt).
- `username`: Tên đăng nhập (Unique).
- `email`: Địa chỉ email (Unique).
- `password`: Mật khẩu mã hóa (BCrypt).
- `full_name`: Tên đầy đủ.
- `role`: Vai trò (`USER`, `ADMIN`).

### 2. Bảng `decks` (Bộ thẻ)
- `id`: Khóa chính.
- `title`: Tên bộ thẻ (Ví dụ: 6000 Từ vựng TOPIK).
- `description`: Mô tả bộ thẻ.
- `user_id`: Khóa ngoại (N-1 với `users`).

### 3. Bảng `cards` (Thẻ từ vựng)
- `id`: Khóa chính.
- `front`: Mặt trước (Tiếng Hàn).
- `back`: Mặt sau (Nghĩa).
- `example`: Ví dụ minh họa.
- `status`: Trạng thái học (`NEW`, `LEARNING`, `REVIEW`, `MASTERED`).
- `next_review_date`: Ngày ôn tập tiếp theo.
- `deck_id`: Khóa ngoại (N-1 với `decks`).

---

## 🚀 Lộ trình Phát triển (Roadmap)

### 📍 Giai đoạn 1: Khởi tạo và Cấu hình cốt lõi (Hoàn thành ✅)
*   Setup Spring Boot, JPA, Security.
*   Cấu hình cơ sở dữ liệu và JWT.

### 📍 Giai đoạn 2: Lập trình Backend API (Hoàn thành ✅)
*   Hoàn thiện CRUD cho User, Deck, Card.
*   Hiện thực hóa thuật toán Spaced Repetition System (SRS).

### 📍 Giai đoạn 3: Phát triển Giao diện Frontend (Hoàn thành cơ bản ✅)
*   **Hạ tầng:** Setup Vite, Tailwind (hoặc CSS), Axios, Zustand.
*   **Xác thực:** Trang Đăng ký, Đăng nhập, Protected Routes.
*   **Quản lý:** Dashboard quản lý Deck, Explorer quản lý Card.
*   **Học tập:** Giao diện Flashcard 3D và hệ thống đánh giá SRS.

### 📍 Giai đoạn 4: Kiểm thử và Tối ưu hóa cơ bản (Hoàn thành ✅)
1.  **CORS & Security:** Hoàn thiện cấu hình kết nối Front-Back.
2.  **Global Error Handling:** Chuẩn hóa thông báo lỗi giữa Backend và Frontend (Sử dụng `@RestControllerAdvice` và Axios Interceptors).
3.  **API Documentation:** Tích hợp Swagger UI (OpenAPI 3) để tra cứu endpoint tại `/swagger-ui.html`.

### 📍 Giai đoạn 5: Tính năng Nâng cao (Hoàn thành ✅)
1.  **Pagination & Search:** Đã triển khai phân trang và tìm kiếm cho Deck và Card ở cả Backend và Frontend.
2.  **Media Support:** Hỗ trợ lưu trữ URL hình ảnh và âm thanh cho thẻ, tích hợp trình phát âm thanh và hiển thị ảnh trong quá trình học.
3.  **Analytics Dashboard:** Bổ sung biểu đồ thống kê tổng quan về tiến trình học tập, số lượng thẻ theo trạng thái SRS tại Dashboard.
4.  **Social Features:** Tính năng chia sẻ bộ thẻ công khai (Public Decks) và cho phép người dùng khác sao chép (Clone) về tài khoản cá nhân.

### 📍 Giai đoạn 6: Triển khai & DevOps (Hoàn thành ✅)
1.  **Dockerization:** Đã viết `Dockerfile` cho cả Backend và Frontend, tối ưu hóa image size.
2.  **Orchestration:** Sử dụng `docker-compose.yml` để quản lý toàn bộ stack (DB, Backend, Frontend) chỉ với một lệnh.
3.  **CI/CD:** Thiết lập GitHub Actions tự động kiểm thử và build project khi có code mới.
4.  **Logging:** Cấu hình log tập trung giúp theo dõi trạng thái container dễ dàng.

### 📍 Giai đoạn 7: Nâng cao Trải nghiệm & Game hóa (Kế hoạch 🚀)
1.  **Gamification:** Hệ thống tích điểm (XP), cấp độ (Level) và Chuỗi ngày học (Streak) để tăng tính gắn kết của người dùng.
2.  **AI Integration:** Sử dụng AI để tự động tạo câu ví dụ, gợi ý nghĩa và giải thích ngữ pháp cho từ vựng mới.
3.  **Smart Notifications:** Hệ thống nhắc nhở ôn tập qua Email hoặc Web Push khi có thẻ đến hạn (Next Review Date).
4.  **Chế độ học đa dạng:** Bổ sung các chế độ Quiz (Trắc nghiệm), Nghe chọn ảnh, và Luyện viết chính tả.
5.  **Import/Export:** Hỗ trợ nhập dữ liệu từ Excel/CSV hoặc xuất bộ thẻ ra định dạng Anki/PDF.
6.  **PWA (Progressive Web App):** Hỗ trợ cài đặt ứng dụng lên màn hình điện thoại và sử dụng offline cơ bản.

---

## 🔍 Đánh giá Hệ thống Hiện tại

### ✅ Ưu điểm
- Kiến trúc Modular giúp tách biệt rõ ràng logic nghiệp vụ.
- Frontend đáp ứng tốt các luồng học tập cơ bản.
- Hệ thống bảo mật JWT hoạt động ổn định.
- Đã có tính năng tìm kiếm, phân trang và thống kê trực quan.

### ⚠️ Vấn đề cần ưu tiên xử lý
- **Media Upload:** Hiện tại chỉ hỗ trợ URL. Cần tích hợp Cloudinary/S3 để hỗ trợ upload ảnh/âm thanh trực tiếp.
- **UX Details:** Bổ sung thêm Skeleton loading và các hiệu ứng chuyển cảnh mượt mà hơn.
- **Testing:** Cần bổ sung thêm Unit Test cho các trường hợp biên của thuật toán SRS.
