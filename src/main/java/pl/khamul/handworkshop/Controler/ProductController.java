package pl.khamul.handworkshop.Controler;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.khamul.handworkshop.entity.CartItem;
import pl.khamul.handworkshop.entity.ShoppingCart;
import pl.khamul.handworkshop.entity.Product;
import pl.khamul.handworkshop.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private ShoppingCart cart;


    public ProductController(ProductRepository productRepository, ShoppingCart cart){
        this.productRepository = productRepository;
        this.cart = cart;



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
        return  "/confirm";
    }

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("list", productRepository.findAll());
        return "/list";
    }
    @GetMapping ("/addtocart/{id}")
    public String addToCart(@PathVariable("id") Long toAdd, HttpServletRequest request){
        HttpSession session = request.getSession(false);
            if(session == null){
                session = request.getSession();
            }
            if(session.getAttribute("cart") != null){
                cart = (ShoppingCart)session.getAttribute("cart");
            }
        CartItem cartItem = new CartItem(productRepository.findById(toAdd).get(), 1);
        cart.addToCart(cartItem) ;
        session.setAttribute("cart", cart);



        return "redirect:/list";
    }


}
