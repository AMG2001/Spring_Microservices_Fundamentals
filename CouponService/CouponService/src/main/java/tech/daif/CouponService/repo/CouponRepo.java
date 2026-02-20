package tech.daif.CouponService.repo;

import org.springframework.stereotype.Repository;
import tech.daif.CouponService.exceptions.CouponAlreadyExistsException;
import tech.daif.CouponService.exceptions.CouponNotExistsException;
import tech.daif.CouponService.model.Coupon;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CouponRepo {

    private Map<String, Coupon> couponsDB = new HashMap<>();

    public CouponRepo(){
        couponsDB.put("10_SALE",new Coupon("10_SALE",.1));
        couponsDB.put("20_SALE",new Coupon("20_SALE",.2));
        couponsDB.put("30_SALE",new Coupon("30_SALE",.3));
        couponsDB.put("SUPER_SALE",new Coupon("SUPER_SALE",.5));
    }

    public void addCoupon(Coupon coupon){
        if(couponsDB.containsKey(coupon.couponName())) throw new CouponAlreadyExistsException();
        else couponsDB.put(coupon.couponName(),coupon);
    }

    public Coupon getCoupon(String couponName){
        if(!couponsDB.containsKey(couponName)) throw new CouponNotExistsException();
        return couponsDB.get(couponName);
    }
}
