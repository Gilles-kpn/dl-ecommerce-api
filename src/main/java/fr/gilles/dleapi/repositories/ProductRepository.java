package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.products.Category;
import fr.gilles.dleapi.entities.products.Product;
import fr.gilles.dleapi.entities.products.TransactionStatus;
import fr.gilles.dleapi.entities.user.Admin;
import fr.gilles.dleapi.payloader.response.CountStats;
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
    Page<Product> findByNameContainsAndDeletedAndStatus(String name, Pageable pageable, boolean deleted , TransactionStatus status);
    Page<Product> findByNameContainsAndDeletedAndAuthorAndStatus(String name, boolean deleted, Admin author, Pageable pageable, TransactionStatus status);
    Optional<Product> findByCode(String code);
    Page<Product> findByCategoryAndDeletedAndStatus(Category category,boolean deleted, Pageable pageable, TransactionStatus status);
    Page<Product> findByAuthorAndDeletedAndStatus(Admin author, boolean deleted, Pageable pageable,TransactionStatus status);
    Page<Product> findByCategoryIsInAndDeletedAndStatus(Set<Category> categories,boolean deleted, Pageable pageable,TransactionStatus status);
    int countAllByDeleted(boolean deleted);
    Page<Product> findByDeletedOrCategoryIsInOrPriceBetweenAndStatus(boolean deleted,Set<Category> categories, int priceMin, int priceMax, Pageable pageable,TransactionStatus status);
    @Query("select  " +
            "new fr.gilles.dleapi.payloader.response.CountStats(" +
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
            "new fr.gilles.dleapi.payloader.response.CountStats(" +
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
