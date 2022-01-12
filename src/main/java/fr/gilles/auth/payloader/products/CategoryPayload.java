package fr.gilles.auth.payloader.products;

import fr.gilles.auth.entities.products.Category;
import fr.gilles.auth.payloader.Payloader;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class CategoryPayload extends Payloader<Category> {
    @NotNull
    @NotBlank
    @NotEmpty
    private  String name;
    private  String description;

    @Override
    public Category toModel() {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return  category;
    }
}
