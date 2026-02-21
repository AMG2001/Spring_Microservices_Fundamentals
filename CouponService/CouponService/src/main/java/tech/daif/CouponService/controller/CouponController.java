package tech.daif.CouponService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.daif.CouponService.model.Coupon;
import tech.daif.CouponService.service.CouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{couponName}")
    public Coupon getCoupon(@PathVariable String couponName) {
        return couponService.getCoupon(couponName);
    }
}
