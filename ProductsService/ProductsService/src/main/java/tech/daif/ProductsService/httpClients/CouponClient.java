package tech.daif.ProductsService.httpClients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.daif.ProductsService.model.Coupon;

@FeignClient("COUPONSERVICE")
public interface CouponClient {

    @GetMapping("/coupon/{couponName}")
    Coupon getCoupon(@PathVariable String couponName);

}
