package tech.daif.ProductsService.controller;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.daif.ProductsService.model.Product;
import tech.daif.ProductsService.model.ProductRequest;
import tech.daif.ProductsService.service.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Retry(name = "getProductRetry", fallbackMethod = "fallbackMethod")
    public ResponseEntity<Product> getProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.getProduct(productRequest.productName(), productRequest.couponName()));
    }

    public ResponseEntity<Product> fallbackMethod(
            @RequestBody ProductRequest productRequest,  // Same as original
            Exception ex
    ) {
        // Your fallback logic
        return ResponseEntity.ok(productService.getProduct(productRequest.productName(), productRequest.couponName()));
    }
}
