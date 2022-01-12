package fr.gilles.auth.services.product;

import fr.gilles.auth.entities.products.Category;
import fr.gilles.auth.payloader.query.QueryParams;
import fr.gilles.auth.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Optional<Category> findByName(@NotNull @NotBlank String name){
        return  categoryRepository.findByName(name);
    }


    public Category create(@NotNull @Validated Category categoryCreate){
        Optional<Category> categoryOptional = findByName(categoryCreate.getName());
        if (categoryOptional.isEmpty())
            return  categoryRepository.save(categoryCreate);
        else {
            Category category = categoryOptional.get();
            if (category.isDeleted()){
                category.setDeleted(false);
                category.setDescription(categoryCreate.getDescription());
                return  categoryRepository.save(category);
            }else{
                return  null;
            }
        }
    }


    public Page<Category> all(QueryParams queryParams){
        return  categoryRepository.findAllBy(queryParams.toPageRequest());
    }


    public void delete(@NotNull @Validated Category categoryToDelete){
        Optional<Category> optionalCategory = findByName(categoryToDelete.getName());
        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setDeleted(true);
            categoryRepository.save(category);
        }
    }


}
