package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.products.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}
