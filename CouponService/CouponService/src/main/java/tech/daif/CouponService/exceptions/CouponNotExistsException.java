package tech.daif.CouponService.exceptions;

public class CouponNotExistsException extends IllegalArgumentException {

    private String message = "There is no coupon with this name";

    @Override
    public String getMessage() {
        return message;
    }
}
