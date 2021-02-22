package pl.khamul.handworkshop.controler;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.khamul.handworkshop.entity.*;
import pl.khamul.handworkshop.repository.*;
import pl.khamul.handworkshop.service.CartService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
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
    private final ReservationRepo reservationRepo;
    private final UserRepository userRepository;
    private final AdresRepository adresRepository;
    private final CartService cartService;


    public CartController(ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository, OrderHistoryRepository orderHistoryRepository, ReservationRepo reservationRepo, UserRepository userRepository, AdresRepository adresRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.reservationRepo = reservationRepo;
        this.userRepository = userRepository;
        this.adresRepository = adresRepository;
        this.cartService = cartService;
    }

    @GetMapping("")
    public String list(Model model, HttpSession session) {
        ShoppingCart temp = cartService.getCart(session);
        if (temp == null) {
            model.addAttribute("cart", new ArrayList<CartItem>());
        } else {
            model.addAttribute("cart", temp.getItems());
            double sum = cartService.totalPrice(temp);
            model.addAttribute("total", sum);
        }

        return "/basket";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, HttpSession session){

        cart = cartService.getCart(session);
        List<CartItem> list = cart.getItems();


        CartItem cartItem = list.stream()
                .filter(x -> id.equals(x.getProduct().getId()))
                .findFirst().get();

        cart.setItems(list.stream()
                .filter(x -> !id.equals(x.getProduct().getId()))
                .collect(Collectors.toList()));


        ReservationItem reservationItem = reservationRepo.findByProductId(id);
        reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() - cartItem.getQuantity());
        reservationRepo.save(reservationItem);


        session.setAttribute("cart", cart);

        return "/confirm";

    }
    @RequestMapping("/update/{id}/{quantity}")
    public String updateItem(@PathVariable Long id, @PathVariable int quantity, HttpSession session){

        cart = cartService.getCart(session);
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

    @RequestMapping("/order")
    public String createOrder(HttpSession session, Model model, HttpServletRequest request){
        ShoppingCart temp = cartService.getCart(session);
        Principal principal = request.getUserPrincipal();

        if (temp == null) {
            model.addAttribute("cart", new ArrayList<CartItem>());
        } else {
            model.addAttribute("cart", temp.getItems());
            double sum = cartService.totalPrice(temp);
            model.addAttribute("total", sum);
        }

        if(principal !=null) {
            User user = userRepository.findFirstByEmail(principal.getName());
            List<Adress> list = user.getAdres();
            model.addAttribute("adres", list);
        }else{
            List<Adress> list= new ArrayList<>();
            model.addAttribute("adres", list);
        }






        return "/order";


    }

    @RequestMapping("/save/{id}")
    public String saveOrder(HttpSession session, HttpServletRequest request, @PathVariable Long id){

        Adress adress = adresRepository.getOne(id);

        ShoppingCart save = cartService.getCart(session);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setProductList(save);
        orderHistory.setOrderDate(LocalDateTime.now());
        double sum =cartService.totalPrice(save);

        orderHistory.setAdres(adress);
        orderHistory.setPaid(sum);


        for (CartItem x : save.getItems()){
            ReservationItem reservationItem = reservationRepo.findByProductId(x.getProduct().getId());
            reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() - x.getQuantity());
            reservationRepo.save(reservationItem);
        }
        Principal principal = request.getUserPrincipal();

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

    @RequestMapping("/save")
    public String saveOrder(HttpSession session, HttpServletRequest request, @ModelAttribute Adress adress){


        adresRepository.save(adress);
        ShoppingCart save = cartService.getCart(session);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setProductList(save);
        orderHistory.setOrderDate(LocalDateTime.now());
        double sum = cartService.totalPrice(save);
        orderHistory.setAdres(adress);
        orderHistory.setPaid(sum);



        for (CartItem x : save.getItems()){
            ReservationItem reservationItem = reservationRepo.findByProductId(x.getProduct().getId());
            reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() - x.getQuantity());
            reservationRepo.save(reservationItem);
        }
        Principal principal = request.getUserPrincipal();

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



    @RequestMapping("/plus/{id}")
    public String plusOne(@PathVariable Long id, HttpSession session){

        cart = cartService.getCart(session);
        List<CartItem> list = cart.getItems();

        CartItem item = list.stream()
                .filter(x -> id.equals(x.getProduct().getId()))
                .findFirst().get();

        Product product = item.getProduct();

        product.setStoragequantity(product.getStoragequantity()-1);

        item.setQuantity(item.getQuantity()+1);


        List <CartItem> NewList =  list.stream()
                .filter(x -> !id.equals(x.getProduct().getId()))
                .collect(Collectors.toList());

        NewList.add(item);

        cart.setItems(NewList);

        ReservationItem reservationItem = reservationRepo.findByProductId(id);
        reservationItem.setReservedQuantity(reservationItem.getReservedQuantity()+1);
        productRepository.save(product);

        session.setAttribute("cart", cart);



        return "/confirm";
    }

    @RequestMapping("/minus/{id}")
    public String minusOne(@PathVariable Long id, HttpSession session){

        cart = cartService.getCart(session);
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
            Product product = item.getProduct();
            product.setStoragequantity(product.getStoragequantity()+1);
            ReservationItem reservationItem = reservationRepo.findByProductId(id);
            reservationItem.setReservedQuantity(reservationItem.getReservedQuantity()-1);
            productRepository.save(product);

            cart.setItems(NewList);


            session.setAttribute("cart", cart);

        }

        return "/confirm";
    }


}
