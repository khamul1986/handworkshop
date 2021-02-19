package pl.khamul.handworkshop.Controler;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.khamul.handworkshop.entity.CartItem;
import pl.khamul.handworkshop.entity.ReservationItem;
import pl.khamul.handworkshop.entity.ShoppingCart;
import pl.khamul.handworkshop.entity.Product;
import pl.khamul.handworkshop.repository.ProductRepository;
import pl.khamul.handworkshop.repository.ReservationRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private ShoppingCart cart;
    List<CartItem> list = new ArrayList<>();
    private final ReservationRepo reservationRepo;

    public ProductController(ProductRepository productRepository, ShoppingCart cart, ReservationRepo reservationRepo) {
        this.productRepository = productRepository;
        this.cart = cart;
        this.reservationRepo = reservationRepo;
    }

    @RequestMapping("")
    public String test(){
        return "/index";
    }

    @GetMapping("/add")
    public String add(){
        return "/add";
    }

    @PostMapping("/add")
    public String save(@ModelAttribute Product product){
        productRepository.save(product);
        ReservationItem reservationItem=new ReservationItem(product, 0L);
        reservationRepo.save(reservationItem);
        return  "/confirm";
    }

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("list", productRepository.findAll());
        return "/list";
    }
    @GetMapping ("/addtocart/{id}") //dodaje podwójne produkty do koszyka, przerobić na mapę(??)
    public String addToCart(@PathVariable("id") Long toAdd, HttpServletRequest request){
        HttpSession session = request.getSession(false);
            if(session == null){
                session = request.getSession();
            }
            if(session.getAttribute("cart") != null){
                cart = (ShoppingCart)session.getAttribute("cart");
                list = cart.getItems();
            }

        CartItem cartItem = new CartItem(productRepository.findById(toAdd).get(), 1);
        Product product = cartItem.getProduct();
        product.setStoragequantity(product.getStoragequantity()-1);

            list.add(cartItem);
            cart.setItems(list); ;

        ReservationItem reservationItem = reservationRepo.findByProductId(toAdd);
        reservationItem.setProduct(product);
        reservationItem.setReservedQuantity(reservationItem.getReservedQuantity()+1);

        reservationRepo.save(reservationItem);
        productRepository.save(product);
        session.setAttribute("cart", cart);




        return "redirect:/list";
    }


}
