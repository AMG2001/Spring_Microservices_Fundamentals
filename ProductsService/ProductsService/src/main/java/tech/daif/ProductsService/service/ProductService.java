package tech.daif.ProductsService.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import tech.daif.ProductsService.httpClients.CouponClient;
import tech.daif.ProductsService.model.Coupon;
import tech.daif.ProductsService.model.Product;
import tech.daif.ProductsService.repo.ProductRepo;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CouponClient couponClient;

    public Product getProduct(String nameOfProduct, String couponName) {

        Product product = productRepo.getProduct(nameOfProduct);

        if (ObjectUtils.isEmpty(couponName)) {
            return product;
        } else {
            Coupon coupon = couponClient.getCoupon(couponName);
            return getProductWithCoupon(product, coupon);
        }

    }

    public Product getProductWithCoupon(Product product, Coupon coupon) {
        Product productWithCoupon = new Product(product);
        double newPrice = product.getPrice() - (product.getPrice() * coupon.percentage());
        productWithCoupon.setPrice(roundPriceWith2Decimals(newPrice));
        return productWithCoupon;
    }

    public double roundPriceWith2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
