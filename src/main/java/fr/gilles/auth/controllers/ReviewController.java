package fr.gilles.auth.controllers;

import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.rating.Review;
import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.payloader.query.QueryParams;
import fr.gilles.auth.services.product.ProductService;
import fr.gilles.auth.services.rating.ReviewService;
import fr.gilles.auth.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("review")
@Tag(name = "Review", description = "Review endpoints")
public class ReviewController {
    private  final ProductService productService;
    private  final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("{productCode}")
    @Operation(summary = "Get review for a product")
    public ResponseEntity<Page<Review>> getReview(@PathVariable @NotNull @NotBlank @NotEmpty String productCode, QueryParams queryParams) {
        Optional<Product> product = productService.find(productCode);
        if(product.isPresent()) {
            return ResponseEntity.ok(reviewService.findByProduct(product.get(), queryParams));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
    }

    @PostMapping("{productCode}")
    @Operation(summary = "Create a review for a product")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Review> create(@PathVariable @NotNull @NotBlank @NotEmpty String productCode, @RequestBody @NotNull @Validated Review review, Authentication authentication) {
        Optional<Product> product = productService.find(productCode);
        User user = userService.findByEmail(authentication.getName());
        if(product.isPresent()) {
            review.setProduct(product.get());
            review.setUser(user);
            return ResponseEntity.ok(reviewService.create(review));
        }else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
    }

    @DeleteMapping("{reviewCode}")
    @Operation(summary = "Delete a review", security = @SecurityRequirement(name = "Bearer Token"))
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @NotBlank @NotEmpty String reviewCode, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        try{
            reviewService.delete(reviewCode, user);
            return  ResponseEntity.ok().build();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Review not found or you don't have the right to delete it");
        }
    }

}
