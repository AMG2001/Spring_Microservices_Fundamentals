package tech.daif.CouponService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.daif.CouponService.model.Coupon;
import tech.daif.CouponService.repo.CouponRepo;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepo couponRepo;

    public Coupon getCoupon(String couponName) {
        return couponRepo.getCoupon(couponName);
    }
}
