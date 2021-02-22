package pl.khamul.handworkshop.service;

import pl.khamul.handworkshop.entity.ShoppingCart;

import javax.servlet.http.HttpSession;

public interface CartServiceInterface {

    double totalPrice(ShoppingCart cart);

    ShoppingCart getCart(HttpSession session);
}
