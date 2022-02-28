package fr.gilles.dleapi.controllers;

import fr.gilles.dleapi.entities.picture.Picture;
import fr.gilles.dleapi.entities.products.Category;
import fr.gilles.dleapi.entities.products.Product;
import fr.gilles.dleapi.entities.rating.Like;
import fr.gilles.dleapi.entities.user.Admin;
import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.payloader.products.ProductPayload;
import fr.gilles.dleapi.payloader.query.ProductQueryParams;
import fr.gilles.dleapi.payloader.response.ExceptionMessage;
import fr.gilles.dleapi.services.pictures.PictureService;
import fr.gilles.dleapi.services.product.CategoryService;
import fr.gilles.dleapi.services.product.ProductService;
import fr.gilles.dleapi.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("product")
@Tag(name = "Product", description = "All endpoints about product")
@AllArgsConstructor
public class ProductController {


    private final ProductService productService;
    private CategoryService categoryService;
    private final UserService userService;
    private final PictureService pictureService;

    @GetMapping
    @Operation(summary = "all products")
    public ResponseEntity<Page<Product>> all(ProductQueryParams queryParams) {
        Set<Category> cats = categoryService.findByNameIn(Arrays.asList(queryParams.getCategories()));
        return ResponseEntity.ok(productService.all(queryParams, cats));
    }

    @GetMapping("{name}/search")
    @Operation(summary = "search product with name")
    public ResponseEntity<Page<Product>> search(@PathVariable @NotNull @NotEmpty @NotBlank String name,
                                                ProductQueryParams queryParams) {
        return ResponseEntity.ok(productService.search(name, queryParams));
    }

    @GetMapping("{code}/get")
    @Operation(summary = "get specific product with code")
    public ResponseEntity<Product> get(@PathVariable @NotNull @NotEmpty @NotBlank String code) {
        return ResponseEntity.of(productService.find(code));
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Create product | ADMIN OR MANAGER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Product> create(@ModelAttribute @NotNull @Validated ProductPayload productPayload, Authentication authentication) throws IOException {
        Optional<Category> categoryOptional = categoryService.findByName(productPayload.getCategoryName());
        Admin admin = (Admin) userService.findByEmail(authentication.getName());
        if (admin != null) {
            if (categoryOptional.isPresent()) {
                Product product = productPayload.toModel();
                product.setCategory(categoryOptional.get());
                product.setAuthor(admin);
                product.setPictures(productService.uploadImages(productPayload.getFiles()));
                return ResponseEntity.ok(productService.create(product));

            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Insufficient rights");

    }

    @PutMapping("{code}/available")
    @Operation(summary = "update product availability | ADMIN OR MANAGER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Product> updateAvailability(@PathVariable @NotNull @NotEmpty @NotBlank String code,
                                                      @RequestParam Boolean available) {
        try {
            return ResponseEntity.ok(productService.updateAvailability(code, available));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND);
        }
    }

    @DeleteMapping("{code}/delete")
    @Operation(summary = "delete product | OR MANAGER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> delete(@PathVariable @NotNull @NotEmpty @NotBlank String code, Authentication authentication) {
        try {
            productService.delete(code);
            return ResponseEntity.ok("Product deleted");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND);
        }
    }


    @PutMapping("{code}/recycle")
    @Operation(summary = "recycle product | MANAGER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Product> recycle(@PathVariable @NotNull @NotEmpty @NotBlank String code, Authentication authentication) {
        try {
            return ResponseEntity.ok(productService.recycle(code));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND);
        }
    }

    @PutMapping("{code}/like")
    @Operation(summary = "like product | USER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> like(@PathVariable @NotNull @NotEmpty @NotBlank String code, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Optional<Product> productOptional = productService.find(code);
        if (user != null && productOptional.isPresent()) {
            if (user.getLikedProducts().stream().noneMatch(p -> p.getCode().equals(code))) {
                productService.like(productOptional.get(), user);
                return ResponseEntity.ok().build();
            } else throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionMessage.PRODUCT_ALREADY_LIKED);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.PRODUCT_ALREADY_LIKED);
    }

    @PutMapping("{code}/unlike")
    @Operation(summary = "unlike product | USER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> unlike(@PathVariable @NotNull @NotEmpty @NotBlank String code, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Optional<Product> productOptional = productService.find(code);
        if (user != null && productOptional.isPresent()) {
            if (user.getLikedProducts().stream().anyMatch(p -> p.getCode().equals(code))) {
                productService.unlike(productOptional.get(), user);
                return ResponseEntity.ok().build();
            } else throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionMessage.PRODUCT_ALREADY_LIKED);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.PRODUCT_ALREADY_LIKED);
    }


    @GetMapping("{code}/likes")
    @Operation(summary = "get product likes")
    public ResponseEntity<Collection<Like>> getLikes(@PathVariable @NotNull @NotEmpty @NotBlank String code) {
        Optional<Product> productOptional = productService.find(code);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.get().getLikes());
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND);
    }

    @GetMapping("{code}/author")
    @Operation(summary = "Get author of product")
    public ResponseEntity<Admin> author(@PathVariable String code) {
        Optional<Product> productOptional = productService.find(code);
        if (productOptional.isPresent()) {
            if (productOptional.get().getAuthor() != null) {
                return ResponseEntity.ok(productOptional.get().getAuthor());
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found author of this product");
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found product");
    }


    @GetMapping("{code}/same/author")
    @Operation(summary = "Get product from same author")
    public ResponseEntity<Page<Product>> sameAuthor(@PathVariable @NotNull @NotEmpty @NotBlank String code, ProductQueryParams queryParams) {
        Optional<Product> productOptional = productService.find(code);
        if (productOptional.isPresent()) {
            if (productOptional.get().getAuthor() != null) {
                return ResponseEntity.ok(productService.fromAuthor(productOptional.get().getAuthor(), queryParams));
            } else return ResponseEntity.ok().build();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found product");
    }


    @GetMapping("admin/current")
    @Operation(summary = "Get current admin product | ADMIN OR MANAGER AUTHORIZATION",
            security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Page<Product>> currentAdminProduct(Authentication authentication,
                                                             ProductQueryParams queryParams) {
        Admin admin = (Admin) userService.findByEmail(authentication.getName());
        if (admin != null) {
            if (admin.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
                return all(queryParams);
            }
            return ResponseEntity.ok(productService.fromAuthor(admin, queryParams));
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot find current admin ");
    }

    @GetMapping("{name}/admin/search")
    @Operation(summary = "Search product by name | ADMIN OR MANAGER AUTHORIZATION",
            security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Page<Product>> searchByName(@PathVariable @NotNull @NotEmpty @NotBlank String name,
                                                      ProductQueryParams queryParams, Authentication authentication) {
        Admin admin = (Admin) userService.findByEmail(authentication.getName());
        if (admin != null) {
            if (admin.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
                return search(name, queryParams);
            }
            return ResponseEntity.ok(productService.search(name, queryParams, admin));
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot find current admin ");
    }


    @PutMapping("{code}/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update product product | ADMIN OR MANAGER AUTHORIZATION",
            security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<Product> update(@PathVariable @NotNull @NotEmpty @NotBlank String code,
                                          @RequestBody ProductPayload productPayload,
                                          Authentication authentication) throws IOException {
        Product product = productService.find(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND));
        Admin admin = (Admin) userService.findByEmail(authentication.getName());
        if (product.getAuthor().equals(admin) || admin.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_MANAGER"))) {
            product.setName(productPayload.getName());
            product.setDescription(productPayload.getDescription());
            product.setPrice(productPayload.getPrice());
            product.setAvailable(productPayload.isAvailable());
            Category category = categoryService.findByName(productPayload.getCategoryName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
            product.setCategory(category);
            Collection<Picture> pictures = productService.uploadImages(productPayload.getFiles());
            if (pictures != null && !pictures.isEmpty()) {
                product.getPictures().forEach(pictureService::delete);
                product.setPictures(pictures);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot upload images");
            return ResponseEntity.ok(productService.save(product));

        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot update product");
    }


}
