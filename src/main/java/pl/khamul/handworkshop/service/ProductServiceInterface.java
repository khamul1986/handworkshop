package pl.khamul.handworkshop.service;

import pl.khamul.handworkshop.entity.Product;

import javax.servlet.http.HttpServletRequest;

public interface ProductServiceInterface {

    void saveProduct(Product product);

    String addToCart(HttpServletRequest request, Long toAdd );
}
