package pl.khamul.handworkshop.service;

import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.entity.CartItem;
import pl.khamul.handworkshop.entity.Product;
import pl.khamul.handworkshop.entity.ReservationItem;
import pl.khamul.handworkshop.entity.ShoppingCart;
import pl.khamul.handworkshop.repository.ProductRepository;
import pl.khamul.handworkshop.repository.ReservationRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements ProductServiceInterface  {

    private final ProductRepository productRepository;
    private final ReservationRepo reservationRepo;
    private ShoppingCart cart;
    List<CartItem> list;

    public ProductService(ProductRepository productRepository, ReservationRepo reservationRepo, ShoppingCart cart, List<CartItem> list) {
        this.productRepository = productRepository;
        this.reservationRepo = reservationRepo;
        this.cart = cart;
        this.list = list;
    }

    public void saveProduct(Product product){
        productRepository.save(product);
        ReservationItem reservationItem=new ReservationItem(product, 0L);
        reservationRepo.save(reservationItem);
    }

    public String addToCart(HttpServletRequest request,Long toAdd ){
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
        if(product.getStoragequantity()>=0) {
            list.add(cartItem);
            cart.setItems(list);

            ReservationItem reservationItem = reservationRepo.findByProductId(toAdd);
            reservationItem.setProduct(product);
            reservationItem.setReservedQuantity(reservationItem.getReservedQuantity() + 1);

            reservationRepo.save(reservationItem);
            productRepository.save(product);
            session.setAttribute("cart", cart);
        } else  {
            return "/error";
        }
        return "redirect:/list";
    }
}
