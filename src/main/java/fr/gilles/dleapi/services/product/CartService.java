package fr.gilles.dleapi.services.product;

import fr.gilles.dleapi.entities.products.CardProduct;
import fr.gilles.dleapi.entities.products.Cart;
import fr.gilles.dleapi.entities.products.Product;
import fr.gilles.dleapi.entities.user.Client;
import fr.gilles.dleapi.repositories.CardProductRepository;
import fr.gilles.dleapi.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class CartService {
    private final CardProductRepository cardProductRepository;
    private final CartRepository cartRepository;


    public  Cart initCart(@NotNull Client client) throws Exception {
        if(client.getCart() == null){
            Cart cart = new Cart();
            cart.setClient(client);
            return cartRepository.save(cart);
        }
        throw new  Exception("Card already init");
    }


    public void addProductToCart(@NotNull  Product product, @Min(1) int quantity, @NotNull  Client client) throws Exception {
        if(client.getCart() != null){
            CardProduct cardProduct = new CardProduct();
            cardProduct.setProduct(product);
            cardProduct.setQuantity(quantity);
            client.getCart().getProducts().add(cardProduct);
            cartRepository.save(client.getCart());
        }else throw new Exception("Cannot add product cart not init");

    }

    public void removeProductFromCart(@NotNull Product product,@NotNull Client client) throws Exception {
        if(client.getCart() != null){

        }else throw new Exception("Cannot add product cart not init");
    }

    public void resetCart(Client client) throws Exception {
       if(client.getCart() != null){
           if(client.getCart().getProducts().size() > 0)
            cardProductRepository.deleteAll(client.getCart().getProducts());
       }
       throw  new Exception("Cannot reset empty card ");
    }



}
