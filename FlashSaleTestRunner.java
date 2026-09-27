package com.example.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class FlashSaleTestRunner implements CommandLineRunner {

    @Autowired
    private FlashSaleService flashSaleService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== BẮT ĐẦU KIỂM THỬ CACHE STAMPEDE (50 REQUESTS ĐỒNG THỜI) ===");
        
        Long targetProductId = 1L;
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();

        // Tạo 50 luồng chạy đồng thời gọi tới cùng 1 id
        for (int i = 0; i < 50; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                flashSaleService.getProduct(targetProductId);
            });
            futures.add(future);
        }

        // Chờ tất cả 50 luồng thực thi xong
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("=== KẾT THÚC KIỂM THỬ ===");
        System.out.println("Tổng thời gian hoàn thành 50 request: " + (endTime - startTime) + " ms");
        System.out.println("Nếu cấu hình sync=true thành công, bạn chỉ thấy DUY NHẤT 1 dòng 'Fetching from Database...' phía trên!");
    }
}
