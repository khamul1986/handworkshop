package pl.khamul.handworkshop.controler;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.khamul.handworkshop.entity.CartItem;
import pl.khamul.handworkshop.entity.ReservationItem;
import pl.khamul.handworkshop.entity.ShoppingCart;
import pl.khamul.handworkshop.entity.Product;
import pl.khamul.handworkshop.repository.ProductRepository;
import pl.khamul.handworkshop.repository.ReservationRepo;
import pl.khamul.handworkshop.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @RequestMapping("")
    public String welcome(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        if(principal != null){
            return "/indexLogged";
        }

        return "/index";
    }

    @GetMapping("/admin/add")
    public String add(){
        return "/add";
    }

    @PostMapping("/admin/add")
    public String save(@ModelAttribute Product product){

        productService.saveProduct(product);

        return  "/confirm";
    }

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("list", productRepository.findAll());
        return "/list";
    }
    @GetMapping ("/addtocart/{id}")
    public String addToCart(@PathVariable("id") Long toAdd, HttpServletRequest request){



        return productService.addToCart(request, toAdd);
    }

}



