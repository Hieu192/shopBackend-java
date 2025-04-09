# 🛒 E-Commerce Backend

Đây là một dự án **E-Commerce Backend API** được xây dựng bằng Java và Spring Boot. Hệ thống hỗ trợ các tính năng cơ bản như đăng ký, đăng nhập, quản lý sản phẩm, đơn hàng, giỏ hàng, phân quyền người dùng, áp dụng mã giảm giá (discount),...

## 🚀 Tính năng chính

- 🔐 Xác thực và phân quyền sử dụng JWT
- 👥 Quản lý người dùng (User Management)
- 📦 Quản lý sản phẩm và danh mục (Products & Categories)
- 🛒 Giỏ hàng và đơn hàng (Cart & Orders)
- 🧾 Bình luận sản phẩm (Comment)
- 💳 Mã giảm giá (Coupon system)
- 📊 Vai trò và quyền hạn người dùng (Role-based Authorization)
- 🗂 RESTful API theo chuẩn MVC

## 🛠️ Công nghệ sử dụng

| Công nghệ       | Mô tả                               |
|-----------------|-------------------------------------|
| Java 17         | Ngôn ngữ lập trình                  |
| Spring Boot     | Framework phát triển backend        |
| Spring Security | Xác thực và phân quyền              |
| JWT             | Token-based Authentication          |
| MySQL           | Cơ sở dữ liệu                       |
| JPA (Hibernate) | ORM – Truy vấn DB dễ dàng           |
| Lombok          | Giảm boilerplate code               |
| Swagger UI      | Tài liệu hóa và test API            |
| Redis           | Cache dữ liệu, tăng tốc độ truy vấn |

## 📁 Cấu trúc thư mục

```bash
shopBackend/
├── .idea/                      # Thư mục cấu hình của IntelliJ IDEA
├── .mvn/                       # Thư mục Maven Wrapper
├── src/
│   └── main/
│       ├── java/
│       │   └── com.hieu.shopBackend/
│       │       ├── components/       # JWT utilities, helper, token, etc.
│       │       ├── config/           # Spring Security, JWT, Swagger config
│       │       ├── controllers/      # REST API endpoints
│       │       ├── dtos/             # Data Transfer Objects
│       │       ├── enums/            # Enum khai báo trạng thái, loại,...
│       │       ├── exceptions/       # Xử lý lỗi và custom exceptions
│       │       ├── mappers/          # Map DTO <-> Entity (MapStruct,...)
│       │       ├── models/           # Entity đại diện bảng DB
│       │       ├── repositories/     # JPA Repositories (tương tác DB)
│       │       ├── services/         # Business logic xử lý nghiệp vụ
│       │       ├── utils/            # Tiện ích dùng chung (format, validate,...)
│       │       └── ShopBackendApplication.java  # File main chạy ứng dụng
│       └── resources/
│           ├── static/               # Tài nguyên tĩnh (ảnh, js, css...)
│           ├── templates/            # Template dùng với Thymeleaf nếu có
│           └── application.yaml      # File cấu hình chính (DB, JWT, port,...)
