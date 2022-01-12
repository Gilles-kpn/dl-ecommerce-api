package fr.gilles.auth.repositories;

import fr.gilles.auth.entities.products.Category;
import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.user.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByDeleted(boolean deleted,Pageable pageable);
    Page<Product> findByNameIsLikeAndDeleted(String name, Pageable pageable,boolean deleted );
    Optional<Product> findByCode(String code);
    Page<Product> findByCategoryAndDeleted(Category category,boolean deleted, Pageable pageable);
    Page<Product> findByAuthorAndDeleted(Admin author, boolean deleted, Pageable pageable);

}