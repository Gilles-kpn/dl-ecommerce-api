package fr.gilles.auth.repositories;

import fr.gilles.auth.entities.products.Category;
import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.user.Admin;
import fr.gilles.auth.payloader.response.CountStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByDeleted(boolean deleted,Pageable pageable);
    Page<Product> findByNameIsLikeAndDeleted(String name, Pageable pageable,boolean deleted );
    Optional<Product> findByCode(String code);
    Page<Product> findByCategoryAndDeleted(Category category,boolean deleted, Pageable pageable);
    Page<Product> findByAuthorAndDeleted(Admin author, boolean deleted, Pageable pageable);
    Page<Product> findByCategoryIsInAndDeleted(Set<Category> categories,boolean deleted, Pageable pageable);
    int countAllByDeleted(boolean deleted);
    Page<Product> findByDeletedOrCategoryIsInOrPriceBetween(boolean deleted,Set<Category> categories, int priceMin, int priceMax, Pageable pageable);
    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(count(product.code),product.createdAt )" +
            "from Product  product  where product.createdAt between  :start and :end group by  to_date(cast(product.createdAt as text ), 'yyyy-MM-dd HH:mm:ss')")
    List<CountStats> createdStats(@Param("start") Date start, @Param("end") Date end);

    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(count(product.code),product.updatedAt )" +
            "from Product  product where  (product.deleted = true)  and product.updatedAt between  :start and :end group by  to_date(cast(product.updatedAt as text), 'yyyy-MM-dd HH:mm:ss') ")
    List<CountStats> deletedStats(@Param("start") Date start, @Param("end") Date end);

}
