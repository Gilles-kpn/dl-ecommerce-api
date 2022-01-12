package fr.gilles.auth.repositories;

import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.rating.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findByProduct(Product product, Pageable pageable);
    void deleteByCode(String code);
    Optional<Review> findByCode(String code);
}