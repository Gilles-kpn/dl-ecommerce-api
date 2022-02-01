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
    Page<Product> findByNameContainsAndDeleted(String name, Pageable pageable, boolean deleted );
    Page<Product> findByNameContainsAndDeletedAndAuthor(String name, boolean deleted, Admin author, Pageable pageable);
    Optional<Product> findByCode(String code);
    Page<Product> findByCategoryAndDeleted(Category category,boolean deleted, Pageable pageable);
    Page<Product> findByAuthorAndDeleted(Admin author, boolean deleted, Pageable pageable);
    Page<Product> findByCategoryIsInAndDeleted(Set<Category> categories,boolean deleted, Pageable pageable);
    int countAllByDeleted(boolean deleted);
    Page<Product> findByDeletedOrCategoryIsInOrPriceBetween(boolean deleted,Set<Category> categories, int priceMin, int priceMax, Pageable pageable);
    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(" +
            "count(product.code)," +
            "to_date(cast(product.createdAt as text), 'yyyy-MM-dd') )" +
            "from Product  product  " +
            "where  "+
            "product.createdAt " +
            "between to_date(cast(:start as text), 'yyyy-MM-dd') " +
            "and to_date(cast( :end as text), 'yyyy-MM-dd') " +
            "group by  " +
            "to_date(cast(product.createdAt as text), 'yyyy-MM-dd') ")
    List<CountStats> createdStats(@Param("start") Date start, @Param("end") Date end);

    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(" +
            "count(product.code)," +
            "to_date(cast(product.updatedAt as text), 'yyyy-MM-dd') )" +
            "from Product  product  " +
            "where  (product.deleted = true )" +
            "and " +
            "product.updatedAt " +
            "between to_date(cast(:start as text), 'yyyy-MM-dd') " +
            "and to_date(cast( :end as text), 'yyyy-MM-dd') " +
            "group by  " +
            "to_date(cast(product.updatedAt as text), 'yyyy-MM-dd') ")
    List<CountStats> deletedStats(@Param("start") Date start, @Param("end") Date end);

}
