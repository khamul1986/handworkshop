package pl.khamul.handworkshop.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.entity.*;
import pl.khamul.handworkshop.repository.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CartService implements CartServiceInterface {

    private final ReservationRepo reservationRepo;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UnregisteredOrderRepository unregisteredOrderRepository;
    private final AdresService adresService;


    public CartService(ReservationRepo reservationRepo, ProductRepository productRepository,
                       UserRepository userRepository, OrderHistoryRepository orderHistoryRepository, ShoppingCartRepository shoppingCartRepository,
                       UnregisteredOrderRepository unregisteredOrderRepository, AdresService adresService) {
        this.reservationRepo = reservationRepo;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.unregisteredOrderRepository = unregisteredOrderRepository;
        this.adresService = adresService;
    }

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

    @Override
    public void reduceFromReservationOnDelete(Long id, CartItem cartItem) {
        ReservationItem reservationItem = reservationRepo.findByProductId(id);
        Product product = productRepository.getOne(id);
        reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() - cartItem.getQuantity());
        product.setStoragequantity(product.getStoragequantity() + cartItem.getQuantity());
        productRepository.save(product);
    }

    @Override
    public void reduceFromReservationOnPlusOne(Long id, CartItem cartItem) {
        ReservationItem reservationItem = reservationRepo.findByProductId(id);
        Product product = productRepository.getOne(id);
        reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() + 1 );
        product.setStoragequantity(product.getStoragequantity() -1);
        productRepository.save(product);

    }

    @Override
    public void updateReservationOnMinusOne(Long id, CartItem cartItem) {

        ReservationItem reservationItem = reservationRepo.findByProductId(id);
        Product product = productRepository.getOne(id);
        reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() - 1 );
        product.setStoragequantity(product.getStoragequantity() + 1 );
        productRepository.save(product);

    }

    @Override
    public CartItem getCartItem(Long id,ShoppingCart cart) {

        List<CartItem> list = cart.getItems();

        CartItem item = list.stream()
                .filter(x -> id.equals(x.getProduct().getId()))
                .findFirst().get();

        return item;
    }

    @Override
    public List<CartItem> prepareNewList(Long id, ShoppingCart cart) {
        List<CartItem> list = cart.getItems();
        List <CartItem> newCart =  list.stream()
                .filter(x -> !id.equals(x.getProduct().getId()))
                .collect(Collectors.toList());
        return newCart;
    }

    @Override
    public String savingOrder(HttpSession session, HttpServletRequest request, Adress adress) {
        ShoppingCart save = getCart(session);
        OrderHistory orderHistory = new OrderHistory();
        Principal principal = request.getUserPrincipal();

        orderHistory.setProductList(save);
        orderHistory.setOrderDate(LocalDateTime.now());
        double sum =totalPrice(save);

        orderHistory.setAdres(adress);
        orderHistory.setPaid(sum);


        for (CartItem x : save.getItems()){
            ReservationItem reservationItem = reservationRepo.findByProductId(x.getProduct().getId());
            reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() - x.getQuantity());
            reservationRepo.save(reservationItem);
        }


        if(principal != null) {
            User user = userRepository.findFirstByEmail(principal.getName());
            List<OrderHistory> list = user.getOrder();
            list.add(orderHistory);
            user.setOrder(list);
            orderHistoryRepository.save(orderHistory);
            userRepository.save(user);

        }else {
            orderHistoryRepository.save(orderHistory);
        }

        shoppingCartRepository.save(save);

        save = new ShoppingCart();
        session.setAttribute("cart", save);

        return "/confirmOrder";
    }

    public String savingAnonymusOrder(HttpSession session, UnregisteredOrder unregisteredOrder){

        ShoppingCart save = getCart(session);


        double sum =totalPrice(save);
        unregisteredOrder.setShoppingCart(save);
        unregisteredOrder.setTotal(sum);




        for (CartItem x : save.getItems()){
            ReservationItem reservationItem = reservationRepo.findByProductId(x.getProduct().getId());
            reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() - x.getQuantity());
            reservationRepo.save(reservationItem);
        }

        shoppingCartRepository.save(save);

        unregisteredOrderRepository.save(unregisteredOrder);





    save = new ShoppingCart();
        session.setAttribute("cart", save);



        return "/confirmOrder";
    }


}


