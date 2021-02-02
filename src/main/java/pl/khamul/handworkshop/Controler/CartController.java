package pl.khamul.handworkshop.Controler;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.khamul.handworkshop.entity.CartItem;
import pl.khamul.handworkshop.entity.ShoppingCart;
import pl.khamul.handworkshop.entity.OrderHistory;
import pl.khamul.handworkshop.repository.OrderHistoryRepository;
import pl.khamul.handworkshop.repository.ProductRepository;
import pl.khamul.handworkshop.repository.ShoppingCartRepository;


import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/basket")
public class CartController {

    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private ShoppingCart cart;
    private final OrderHistoryRepository orderHistoryRepository;


    public CartController(ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCart cart, OrderHistoryRepository orderHistoryRepository) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cart = cart;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @GetMapping("")
    public String list(Model model, HttpSession session) {
        ShoppingCart temp = (ShoppingCart) session.getAttribute("cart");
        if (temp == null) {
            model.addAttribute("cart", new ArrayList<CartItem>() {
            });
        } else {
            model.addAttribute("cart", temp.getItems());
        }

        return "/basket";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, HttpSession session){

        cart = (ShoppingCart)session.getAttribute("cart");
        List<CartItem> list = cart.getItems();

        cart.setItems(list.stream()
                .filter(x -> !id.equals(x.getProduct().getId()))
                .collect(Collectors.toList()));


        session.setAttribute("cart", cart);

        return "/confirm";

    }
    @RequestMapping("/update/{id}/{quantity}")
    public String updateItem(@PathVariable Long id, @PathVariable int quantity, HttpSession session){

        cart = (ShoppingCart)session.getAttribute("cart");
        List<CartItem> list = cart.getItems();

        CartItem item = list.stream()
                .filter(x -> id.equals(x.getProduct().getId()))
                .findFirst().get();
        item.setQuantity(quantity);

        List <CartItem> NewList =  list.stream()
                .filter(x -> !id.equals(x.getProduct().getId()))
                .collect(Collectors.toList());

        NewList.add(item);

        cart.setItems(NewList);


        session.setAttribute("cart", cart);

        return "/confirm";

    }

    @RequestMapping("/save")
    public String saveOrder(HttpSession session){
        ShoppingCart save = (ShoppingCart)session.getAttribute("cart");
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setProductList(save);
        orderHistory.setOrderDate(LocalDateTime.now());

        save.setOrderHistory(orderHistory);

        orderHistoryRepository.save(orderHistory);
        shoppingCartRepository.save(save);

        save = new ShoppingCart();
        session.setAttribute("cart", save);



        return "/confirm";


    }

    @RequestMapping("/plus/{id}")
    public String plusOne(@PathVariable Long id, HttpSession session){

        cart = (ShoppingCart)session.getAttribute("cart");
        List<CartItem> list = cart.getItems();

        CartItem item = list.stream()
                .filter(x -> id.equals(x.getProduct().getId()))
                .findFirst().get();
        item.setQuantity(item.getQuantity()+1);

        List <CartItem> NewList =  list.stream()
                .filter(x -> !id.equals(x.getProduct().getId()))
                .collect(Collectors.toList());

        NewList.add(item);

        cart.setItems(NewList);


        session.setAttribute("cart", cart);



        return "/confirm";
    }

    @RequestMapping("/minus/{id}")
    public String minusOne(@PathVariable Long id, HttpSession session){

        cart = (ShoppingCart)session.getAttribute("cart");
        List<CartItem> list = cart.getItems();

        CartItem item = list.stream()
                .filter(x -> id.equals(x.getProduct().getId()))
                .findFirst().get();
        item.setQuantity(item.getQuantity()-1);

        if(item.getQuantity() == 0){
            deleteItem(item.getProduct().getId(), session);
        }else {

            List<CartItem> NewList = list.stream()
                    .filter(x -> !id.equals(x.getProduct().getId()))
                    .collect(Collectors.toList());

            NewList.add(item);

            cart.setItems(NewList);


            session.setAttribute("cart", cart);
        }

        return "/confirm";
    }
}
