package fr.gilles.auth.payloader.products;

import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.payloader.Payloader;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class ProductPayload extends Payloader<Product>  {
    @NotNull
    @NotEmpty
    @NotBlank
    private  String name;

    @Min(value = 1)
    private  int price;

    @NotNull
    @NotBlank
    @NotEmpty
    private  String categoryName;
    private  boolean available = true;
    private  String description;
    private Collection<MultipartFile>  files;

    @Override
    public Product toModel() {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setAvailable(available);
        product.setDescription(description);
        return  product;
    }
}
