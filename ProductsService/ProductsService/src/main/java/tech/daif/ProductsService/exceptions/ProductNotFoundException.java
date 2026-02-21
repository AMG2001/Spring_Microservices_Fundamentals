package tech.daif.ProductsService.exceptions;

public class ProductNotFoundException extends IllegalArgumentException{

    private String message;

    public ProductNotFoundException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
