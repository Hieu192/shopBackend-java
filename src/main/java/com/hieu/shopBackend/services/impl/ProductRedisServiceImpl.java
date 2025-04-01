package com.hieu.shopBackend.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hieu.shopBackend.dtos.responses.product.ProductResponse;
import com.hieu.shopBackend.services.ProductRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRedisServiceImpl implements ProductRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    // Tạo chuỗi duy nhất làm key trong Redis cho từng request tìm kiếm
    public String getKeyFrom(String keyword,
                             Long categoryId,
                             PageRequest pageRequest,
                             String sortField,
                             String sortDirection) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize =pageRequest.getPageSize();
        return String.format(
                "all_products:%s:%d:%d:%d:%s:%s",
                keyword.toLowerCase(),
                categoryId,
                pageNumber,
                pageSize,
                sortField.toLowerCase(),
                sortDirection.toLowerCase());
    }

    // Xóa toàn bộ dữ liệu Redis
    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    // Khi sản phẩm thay đổi( update, insert) thì dùng hàm này xóa
    public void clearProductCache() {
        redisTemplate.keys("all_products:*").forEach(redisTemplate::delete);
    }

    // Lấy danh sách sản phẩm từ Redis
    // 1. Lấy key từ request
    // 2. Tìm dữ liệu trong redis bằng redisTemplate.opsForValue().get(key)
    // 3. Redis trả về Json, dùng redisObjectMapper.readValue để chuyển Json thành danh sách đối tượng ProductResponse
    @Override
    public List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest, String sortField, String sortDirection) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest, sortField, sortDirection);
        String json = (String) redisTemplate.opsForValue().get(key);
        return (json != null)
                ? redisObjectMapper.readValue(json, new TypeReference<>() {})
                : null;
    }

    // Lưu danh sách sản phẩm vào redis khi get lần đầu
    // 1. Tạo key từ request
    // 2. chuyển danh sách ProductResponse thành Json bằng redisObjectMapper.writeValueAsString để lưu vào redis
    // 3. Lưu vào redis bằng redisTemplate.opsForValue().set(key, json);
    @Override
    public void saveAllProducts(List<ProductResponse> productResponses, String keyword, Long categoryId, PageRequest pageRequest, String sortField, String sortDirection) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest, sortField, sortDirection);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}
