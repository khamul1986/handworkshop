package pl.khamul.handworkshop.Controler;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.khamul.handworkshop.entity.*;
import pl.khamul.handworkshop.repository.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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


    public CartController(ProductRepository productRepository, ShoppingCartRepository shoppingCartRepository,
                          ShoppingCart cart, OrderHistoryRepository orderHistoryRepository,
                          ReservationRepo reservationRepo, UserRepository userRepository, AdresRepository adresRepository) {
        this.productRepository = productRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cart = cart;
        this.orderHistoryRepository = orderHistoryRepository;
        this.reservationRepo = reservationRepo;
        this.userRepository = userRepository;
        this.adresRepository = adresRepository;
    }

    @GetMapping("")
    public String list(Model model, HttpSession session) {
        ShoppingCart temp = (ShoppingCart) session.getAttribute("cart");
        if (temp == null) {
            model.addAttribute("cart", new ArrayList<CartItem>());
        } else {
            model.addAttribute("cart", temp.getItems());
            double sum = 0;
            for (CartItem x: cart.getItems()) {
                sum= sum+(x.getProduct().getPrice().longValue()*x.getQuantity());

            }
            model.addAttribute("total", sum);
        }

        return "/basket";
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, HttpSession session){

        cart = (ShoppingCart)session.getAttribute("cart");
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

    @RequestMapping("/order")//wydzielić na spokojnie
    public String createOrder(HttpSession session, Model model, HttpServletRequest request){ //wypakować z sesji i policzyć na nowo, dodać adres formularzem
        ShoppingCart temp = (ShoppingCart) session.getAttribute("cart");
        if (temp == null) {
            model.addAttribute("cart", new ArrayList<CartItem>());
        } else {
            model.addAttribute("cart", temp.getItems());
            double sum = 0;
            for (CartItem x: cart.getItems()) {
                sum= sum+(x.getProduct().getPrice().longValue()*x.getQuantity());

            }
            model.addAttribute("total", sum);
        }
        Principal principal = request.getUserPrincipal();
        if(principal !=null) {
            User user = userRepository.findFirstByEmail(principal.getName());
            List<Adres> list = user.getAdres();
            System.out.println(list);
            model.addAttribute("adres", list);
        }else{
            List<Adres> list= new ArrayList<>();
            System.out.println(list);
            model.addAttribute("adres", list);
        }






        return "/order";


    }

    @RequestMapping("/save/{id}")
    public String saveOrder(HttpSession session, HttpServletRequest request, @PathVariable Long id){

        Adres adres = adresRepository.getOne(id);

        ShoppingCart save = (ShoppingCart)session.getAttribute("cart");
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setProductList(save);
        orderHistory.setOrderDate(LocalDateTime.now());
        double sum = 0;
        for (CartItem x: cart.getItems()) {
            sum= sum+(x.getProduct().getPrice().longValue()*x.getQuantity());

        }

        orderHistory.setAdres(adres);
        orderHistory.setPaid(sum);

        save.setOrderHistory(orderHistory);

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
    public String saveOrder(HttpSession session, HttpServletRequest request, @ModelAttribute Adres adres){

        System.out.println(adres.getCity());
        adresRepository.save(adres);
        ShoppingCart save = (ShoppingCart)session.getAttribute("cart");
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setProductList(save);
        orderHistory.setOrderDate(LocalDateTime.now());
        double sum = 0;
        for (CartItem x: cart.getItems()) {
            sum= sum+(x.getProduct().getPrice().longValue()*x.getQuantity());

        }
        orderHistory.setAdres(adres);
        orderHistory.setPaid(sum);

        save.setOrderHistory(orderHistory);

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

        cart = (ShoppingCart)session.getAttribute("cart");
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
