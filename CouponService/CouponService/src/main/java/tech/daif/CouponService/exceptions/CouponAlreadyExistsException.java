package tech.daif.CouponService.exceptions;

public class CouponAlreadyExistsException extends IllegalArgumentException{

    private String message = "Coupon with this name already exists";

    @Override
    public String getMessage() {
        return message;
    }

    public CouponAlreadyExistsException(){

    }
}
