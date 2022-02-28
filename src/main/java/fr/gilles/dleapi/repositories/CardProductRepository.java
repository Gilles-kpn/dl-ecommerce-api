package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.products.CardProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, Integer> {
}