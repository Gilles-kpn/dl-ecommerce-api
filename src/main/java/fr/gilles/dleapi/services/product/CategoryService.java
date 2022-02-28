package fr.gilles.dleapi.services.product;

import fr.gilles.dleapi.entities.products.Category;
import fr.gilles.dleapi.payloader.query.QueryParams;
import fr.gilles.dleapi.payloader.response.CountStats;
import fr.gilles.dleapi.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Optional<Category> findByName(@NotNull @NotBlank String name){
        return  categoryRepository.findByName(name);
    }


    public Page<Category> findByNameContaining(String name, QueryParams queryParams){
        return  categoryRepository.findByNameContainingAndDeleted(name,queryParams.isDeleted(),queryParams.toPageRequest());
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
        return  categoryRepository.findAllByDeleted(queryParams.toPageRequest(),queryParams.isDeleted());
    }


    public void delete(@NotNull @Validated Category categoryToDelete){
        Optional<Category> optionalCategory = findByName(categoryToDelete.getName());
        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setDeleted(true);
            categoryRepository.save(category);
        }
    }


    public void recycle(@NotNull @Validated Category categoryToRecycle){
        Optional<Category> optionalCategory = findByName(categoryToRecycle.getName());
        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setDeleted(false);
            categoryRepository.save(category);
        }
    }



    public Set<Category> findByNameIn(List<String> categories){
        return  categoryRepository.findByNameInAndDeleted(categories,false);
    }

    public int count( boolean deleted){
        return  categoryRepository.countAllByDeleted(deleted);
    }

    public List<CountStats> createdStats(Date start, Date end){
        return  categoryRepository.createdStats(start, end);
    }

    public List<CountStats> deletedStats(Date start, Date end){
        return  categoryRepository.deletedStats(start, end);
    }



}