package fr.gilles.auth.repositories;

import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.rating.Like;
import fr.gilles.auth.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByProductAndUser(Product product, User user);
}