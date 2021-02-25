package pl.khamul.handworkshop.service;

import pl.khamul.handworkshop.entity.Adress;
import pl.khamul.handworkshop.entity.CartItem;
import pl.khamul.handworkshop.entity.ShoppingCart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface CartServiceInterface {

    double totalPrice(ShoppingCart cart);

    ShoppingCart getCart(HttpSession session);

    void reduceFromReservationOnDelete(Long id, CartItem cartItem);

    void reduceFromReservationOnPlusOne(Long id, CartItem cartItem);

    void updateReservationOnMinusOne(Long id, CartItem cartItem);

    CartItem getCartItem(Long id, ShoppingCart cart);

    List<CartItem> prepareNewList(Long id, ShoppingCart cart);

   String savingOrder(HttpSession session, HttpServletRequest request, Adress adress);






}
