package fr.gilles.dleapi.services.rating;

import fr.gilles.dleapi.entities.products.Product;
import fr.gilles.dleapi.entities.rating.Review;
import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.payloader.query.QueryParams;
import fr.gilles.dleapi.repositories.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;


    public Page<Review> findByProduct(Product product, QueryParams queryParams) {
        return reviewRepository.findByProduct(product, queryParams.toPageRequest());
    }

    public Review create(@NotNull Review review) throws IllegalArgumentException {
        if(review.getProduct() != null)
            return reviewRepository.save(review);
        else throw new IllegalArgumentException("Product is required");
    }

    public void delete(String reviewCode, User user) throws IllegalArgumentException {
        Optional<Review> review = reviewRepository.findByCode(reviewCode);
        if(review.isPresent())
            if(review.get().getUser().equals(user))reviewRepository.delete(review.get());
            else throw new IllegalArgumentException("User is not the owner of the review");
        else throw new IllegalArgumentException("Review not found");
    }
}
