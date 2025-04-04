package com.hieu.shopBackend.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hieu.shopBackend.dtos.requests.product.ProductCreateRequest;
import com.hieu.shopBackend.dtos.requests.product.ProductImageRequest;
import com.hieu.shopBackend.dtos.responses.ApiResponse;
import com.hieu.shopBackend.dtos.responses.product.ProductPageResponse;
import com.hieu.shopBackend.dtos.responses.product.ProductResponse;
import com.hieu.shopBackend.models.Product;
import com.hieu.shopBackend.models.ProductImage;
import com.hieu.shopBackend.services.ProductRedisService;
import com.hieu.shopBackend.services.ProductService;
import com.hieu.shopBackend.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import com.github.javafaker.Faker;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRedisService productRedisService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        Product newProduct = productService.createProduct(productCreateRequest);
        return ResponseEntity.ok(
                ApiResponse.builder().success(true)
                        .message(MessageKeys.CREATE_PRODUCT_SUCCESS)
                        .result(newProduct)
                        .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/upload/test")
    public ResponseEntity<String> testUpload(@RequestParam("files") List<MultipartFile> files) {
        System.out.println("hello test upload");
        // Bạn có thể in ra số lượng file nhận được để kiểm tra
        System.out.println("Số file nhận được: " + files.size());
        return ResponseEntity.ok("Test OK");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(
            value = "/uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {
        //1. Nhận ID sản phẩm và danh sách file từ request.
        //2. Kiểm tra xem sản phẩm có tồn tại không.
        //3. Kiểm tra số lượng file có vượt quá giới hạn không.
        //4. Kiểm tra kích thước (<= 10MB) và định dạng (image/*).
        //5. Lưu file vào server hoặc cloud.
        //6. Lưu đường dẫn vào database.
        //7. Trả về danh sách ảnh đã lưu hoặc thông báo lỗi nếu có vấn đề.
        Product existsProduct = productService.getProductById(productId);
        System.out.println("hello upload 1");
        files = (files == null) ? new ArrayList<>() : files;
        System.out.println("hello upload 1 dd" + files);
        if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(MessageKeys.FILES_REQUIRED).build());
        }
        System.out.println("hello upload2");
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }
            System.out.println("hello upload 3");
            // Kiểm tra kích thước và định dạng file
            if (file.getSize() > 10 * 1024 * 1024) { // kích thước lớn hơn 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
                        ApiResponse.builder().error(MessageKeys.FILES_IMAGES_SIZE_FAILED).build()
                );
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(ApiResponse.builder().error(MessageKeys.FILES_IMAGES_TYPE_FAILED).build());
            }
            System.out.println("hello upload 4");
            // lưu file và cập nhật thumnail trong DTO
            String fileName = storeFile(file);
            // lưu vào đối tượng product trong DB ->
            ProductImage productImage = productService.createProductImage(
                    existsProduct.getId(),
                    ProductImageRequest.builder().imageUrl(fileName).build()
            );
            productImages.add(productImage);
        }
        System.out.println("hello upload 5" + productImages);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(MessageKeys.FILES_IMAGES_SUCCESS)
                        .result(productImages)
                        .build()
        );
    }

    @GetMapping("/images/{image-name}")
    public ResponseEntity<?> viewImage(@PathVariable("image-name") String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());

            if (urlResource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri().toString()));
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<ProductPageResponse> getProduct(
            @RequestParam(defaultValue = "", name = "keyword") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam(defaultValue = "id", name = "sort_field") String sortField,
            @RequestParam(defaultValue = "asc", name = "sort_direction") String sortDirection
    ) throws JsonProcessingException {
        PageRequest pageRequest = PageRequest.of(page, limit);

        // Tìm trong redis có db không
        List<ProductResponse> productResponses = productRedisService.getAllProducts(
                keyword, categoryId, pageRequest, sortField, sortDirection);

        if (productResponses == null) {
            Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest, sortField, sortDirection);
            List<ProductResponse> products = productPage.getContent();

            // Lưu sản phẩm vào Redis cache nếu không tìm thấy trong Redis
            productRedisService.saveAllProducts(products, keyword, categoryId, pageRequest, sortField, sortDirection);

            return ResponseEntity.ok(ProductPageResponse.builder()
                    .products(products)
                    .pageNumber(page)
                    .totalElements(productPage.getTotalElements())
                    .pageSize(productPage.getSize())
                    .isLast(productPage.isLast())
                    .totalPages(productPage.getTotalPages())
                    .build());
        }

        // Trường hợp tìm thấy sản phẩm trong Redis
        Page<ProductResponse> productPage = new PageImpl<>(
                productResponses, pageRequest, productResponses.size());
        return ResponseEntity.ok(ProductPageResponse.builder()
                .products(productResponses)
                .pageNumber(page)
                .totalElements(productPage.getTotalElements())
                .pageSize(productPage.getSize())
                .isLast(productPage.isLast())
                .totalPages(productPage.getTotalPages())
                .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            Product existsProducts = productService.getProductById(id);
            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .result(ProductResponse.fromProduct(existsProducts)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(MessageKeys.GET_INFORMATION_FAILED)
                    .error(e.getMessage()).build()
            );
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @GetMapping("/details")
    public ResponseEntity<?> getProductDetailsById(@RequestParam("id") Long id) {
        try {
            Product existsProducts = productService.getDetailProducts(id);
            return ResponseEntity.ok(ApiResponse.builder().success(true).result(existsProducts).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(MessageKeys.GET_INFORMATION_FAILED)
                    .error(e.getMessage()).build());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        try {
            // tách ids thành mảng các số nguyên
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(ApiResponse.builder().success(true).result(products).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(MessageKeys.GET_INFORMATION_FAILED)
                    .error(e.getMessage()).build());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,
                                           @RequestBody ProductCreateRequest productCreateRequest
    ) {
        try {
            Product updateProduct = productService.updateProduct(id, productCreateRequest);
            return ResponseEntity.ok(ApiResponse.builder().success(true).result(updateProduct).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(MessageKeys.MESSAGE_UPDATE_GET)
                    .error(e.getMessage()).build());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message(MessageKeys.MESSAGE_DELETE_SUCCESS + id).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(MessageKeys.MESSAGE_DELETE_FAILED)
                            .error(e.getMessage()).build()
            );
        }
    }


    // Tạo dữ liệu giả
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/generate-faceker-products")
    public ResponseEntity<?> generateFacekerProducts() {
        Faker faker = new Faker(new Locale("vi")); // new Locale("en")
        for (int i = 0; i < 10000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsProduct(productName)) {
                continue;
            }
            ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(2, 7))
                    .build();
            try {
                productService.createProduct(productCreateRequest);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake product generated successfully");
    }


    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image file");
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID() + "_" + fileName;
        // đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        // kiểm tra và tạo thư mục nêú nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // sao chép file vào thư mục
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
