package fr.gilles.auth.repositories;

import fr.gilles.auth.entities.products.Category;
import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.payloader.response.CountStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    Page<Category> findByNameContainingAndDeleted(String name,boolean deleted, Pageable pageable);
    Page<Category> findAllBy(Pageable pageable, boolean deleted);
    Set<Category> findByNameIn(List<String> name, boolean deleted);
    int countAllByDeleted(boolean deleted);
    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(" +
            "count(category.code)," +
            "to_date(cast(category.createdAt as text), 'yyyy-MM-dd') )" +
            "from Category  category  " +
            "where  "+
            "category.createdAt " +
            "between to_date(cast(:start as text), 'yyyy-MM-dd') " +
            "and to_date(cast( :end as text), 'yyyy-MM-dd') " +
            "group by  " +
            "to_date(cast(category.createdAt as text), 'yyyy-MM-dd') ")
    List<CountStats> createdStats(@Param("start") Date start, @Param("end") Date end);

    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(" +
            "count(category.code)," +
            "to_date(cast(category.updatedAt as text), 'yyyy-MM-dd') )" +
            "from Category  category  " +
            "where  (category.deleted = true )" +
            "and " +
            "category.updatedAt " +
            "between to_date(cast(:start as text), 'yyyy-MM-dd') " +
            "and to_date(cast( :end as text), 'yyyy-MM-dd') " +
            "group by  " +
            "to_date(cast(category.updatedAt as text), 'yyyy-MM-dd') ")
    List<CountStats> deletedStats(@Param("start") Date start, @Param("end") Date end);
}
