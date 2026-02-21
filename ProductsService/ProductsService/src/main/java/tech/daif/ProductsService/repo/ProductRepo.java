package tech.daif.ProductsService.repo;

import lombok.val;
import org.springframework.stereotype.Repository;
import tech.daif.ProductsService.exceptions.ProductNotFoundException;
import tech.daif.ProductsService.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepo {

    private ArrayList<Product> productsList = new ArrayList<>();

    private static final String NO_PRODUCT_FOUND_MESSAGE = "There is no product with this name";

    public ProductRepo(){
        productsList.addAll(List.of(
           new Product("Apple Watch","Description 1",99.99),
           new Product("Iphone 17","Iphone 17 Desc",1099.99),
           new Product("Iphone 17 Pro Max","Iphone 17 Pro Max Desc",1099.99)
        ));
    }

    public Product getProduct(String nameOfProduct){
        Optional<Product> foundProduct =  productsList.stream().filter(product -> product.getName().equalsIgnoreCase(nameOfProduct)).findFirst();
        if(foundProduct.isEmpty()) {
            throw new ProductNotFoundException(NO_PRODUCT_FOUND_MESSAGE);
        }
        return foundProduct.get();
    }
}
