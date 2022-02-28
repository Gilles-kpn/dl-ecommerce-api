package fr.gilles.dleapi.payloader.products;

import fr.gilles.dleapi.entities.products.Category;
import fr.gilles.dleapi.payloader.Payloader;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
