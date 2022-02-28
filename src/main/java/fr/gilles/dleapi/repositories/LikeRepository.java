package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.products.Product;
import fr.gilles.dleapi.entities.rating.Like;
import fr.gilles.dleapi.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByProductAndUser(Product product, User user);
}
