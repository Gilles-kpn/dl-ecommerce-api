package fr.gilles.dleapi.controllers;

import fr.gilles.dleapi.entities.products.Product;
import fr.gilles.dleapi.entities.user.Client;
import fr.gilles.dleapi.payloader.response.ExceptionMessage;
import fr.gilles.dleapi.services.product.CartService;
import fr.gilles.dleapi.services.product.ProductService;
import fr.gilles.dleapi.services.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("cart")
@SecurityRequirement(name = "Bearer Token")
@PreAuthorize("hasRole('ROLE_USER')")
@AllArgsConstructor
public class CartController {
    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;


    @PostMapping
    public ResponseEntity<Void> addProduct(
            @RequestBody String productCode,
            @RequestBody @Min(1) int number ,
            Authentication authentication){
        Client client = (Client) userService.findByEmail(authentication.getName());
        if(client != null){
            Optional<Product> product = productService.find(productCode);
            if(product.isPresent()){
                try {
                    cartService.addProductToCart(product.get(),number,client);
                    return  ResponseEntity.ok().build();
                } catch (Exception e) {
                    throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Cart isn't initialize");
                }
            }else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot found product");

        }else throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, ExceptionMessage.UNAUTHORIZED);

    }


}
