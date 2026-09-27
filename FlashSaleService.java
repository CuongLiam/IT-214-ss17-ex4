package com.example.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleService {

    /**
     * BÀI TẬP 4: Chống Cache Stampede bằng sync = true
     * 
     * Khi có 50 request cùng lúc gọi vào method này và Cache đang trống,
     * sync = true đảm bảo CHỈ CÓ 1 THREAD được phép chạy vào hàm này (xuống DB),
     * 49 thread còn lại sẽ phải "đứng chờ" thread đầu tiên hoàn thành và nạp dữ liệu vào Cache.
     * Sau đó 49 thread kia tự động lấy dữ liệu từ Cache ra mà không gọi DB lần nào nữa.
     */
    @Cacheable(value = "flash-sale", key = "#id", sync = true)
    public Product getProduct(Long id) {
        System.out.println("Fetching from Database for product " + id);
        
        try {
            // Giả lập thời gian truy vấn DB mất 2 giây (Mô phỏng query phức tạp)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new Product(id, "Sản phẩm Flash Sale " + id, 99000);
    }

    public static class Product {
        private Long id;
        private String name;
        private double price;

        public Product() {}

        public Product(Long id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}
