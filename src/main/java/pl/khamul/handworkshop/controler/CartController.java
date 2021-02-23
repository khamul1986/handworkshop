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

        CartItem cartItem = cartService.getCartItem(id, cart);

        cart.setItems(cartService.prepareNewList(id, cart));

        cartService.reduceFromReservationOnDelete(id, cartItem);

        session.setAttribute("cart", cart);

        return "/confirm";

    }
    @RequestMapping("/update/{id}/{quantity}") //probowałem wygenerować link w js i thymeleaf, ale nie wsyzlo, restowo
    public String updateItem(@PathVariable Long id, @PathVariable int quantity, HttpSession session){

        cart = cartService.getCart(session);

        CartItem item = cartService.getCartItem(id, cart);
        item.setQuantity(quantity);

        List <CartItem> NewList = cartService.prepareNewList(id, cart);

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

        return cartService.savingOrder(session, request, adress);
    }



    @RequestMapping("/save")
    public String saveOrder(HttpSession session, HttpServletRequest request, @ModelAttribute Adress adress){


        adresRepository.save(adress);
        return cartService.savingOrder(session, request, adress);
    }



    @RequestMapping("/plus/{id}")
    public String plusOne(@PathVariable Long id, HttpSession session){

        cart = cartService.getCart(session);

        CartItem item = cartService.getCartItem(id, cart);

        item.setQuantity(item.getQuantity()+1);

        List <CartItem> newCart =  cartService.prepareNewList(id, cart);

        newCart.add(item);

        cart.setItems(newCart);

        cartService.reduceFromReservationOnPlusOne(id, item);

        session.setAttribute("cart", cart);



        return "/confirm";
    }

    @RequestMapping("/minus/{id}")
    public String minusOne(@PathVariable Long id, HttpSession session){

        cart = cartService.getCart(session);

        CartItem item = cartService.getCartItem(id, cart);
        item.setQuantity(item.getQuantity()-1);

        if(item.getQuantity() == 0){
            deleteItem(item.getProduct().getId(), session);
        }else {

            List<CartItem> newCart = cartService.prepareNewList(id, cart);

            newCart.add(item);

            cartService.updateReservationOnMinusOne(id, item);

            cart.setItems(newCart);

            session.setAttribute("cart", cart);

        }

        return "/confirm";
    }


}
