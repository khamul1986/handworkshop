package pl.khamul.handworkshop.service;

import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.entity.CartItem;
import pl.khamul.handworkshop.entity.ShoppingCart;

import javax.servlet.http.HttpSession;


@Service
public class CartService implements CartServiceInterface {



    public double totalPrice(ShoppingCart cart){
        double sum=0;
        for (CartItem x: cart.getItems()) {
            sum= sum+(x.getProduct().getPrice().longValue()*x.getQuantity());

        }
        return sum;
    }

    public ShoppingCart getCart(HttpSession session){
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        return cart;
    }

}
