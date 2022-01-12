package fr.gilles.auth.controllers;

import fr.gilles.auth.entities.products.Category;
import fr.gilles.auth.payloader.products.CategoryPayload;
import fr.gilles.auth.payloader.query.QueryParams;
import fr.gilles.auth.services.product.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("category")
@AllArgsConstructor
@Tag(name = "Category", description = "Category management")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get available categories")
    public ResponseEntity<Page<Category>> all(QueryParams queryParams){
        return  ResponseEntity.ok(categoryService.all(queryParams));
    }

    @GetMapping("{name}")
    @Operation(summary = "Get category by name")
    public ResponseEntity<Category> findByName(@PathVariable @NotNull @NotBlank @NotEmpty String name){
        return  ResponseEntity.of(categoryService.findByName(name));
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @Operation(summary = "Create new category | MANAGER OR ADMIN ROLE REQUIRED", security = {@SecurityRequirement(name = "Bearer Token")})
    public ResponseEntity<Category> create(@RequestBody CategoryPayload categoryPayload){
        Category category = categoryService.create(categoryPayload.toModel());
        if(category != null)
            return  ResponseEntity.ok(category);
        else {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "Category already exist");
        }
    }


    @DeleteMapping("{name}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Delete category | MANAGER ROLE REQUIRED", security = {@SecurityRequirement(name = "Bearer Token")})
    public ResponseEntity<String> delete(@PathVariable @NotNull @NotBlank @NotEmpty String name){
        Optional<Category> category = categoryService.findByName(name);
        if (category.isPresent()) {
            categoryService.delete(category.get());
            return  ResponseEntity.ok("De");
        }
        else throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete the category "+name);
    }



}
