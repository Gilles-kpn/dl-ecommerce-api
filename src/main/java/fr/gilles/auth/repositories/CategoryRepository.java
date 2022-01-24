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
    Page<Category> findAllBy(Pageable pageable);
    Set<Category> findByNameIn(List<String> name);
    int countAllByDeleted(boolean deleted);
    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(count(category.code),category.createdAt )" +
            "from Category  category  where category.createdAt between  :start and :end group by  DATE_FORMAT(category.createdAt, 'yyyy-MM-dd HH:mm:ss')")
    List<CountStats> createdStats(@Param("start") Date start, @Param("end") Date end);

    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(count(category.code),category.updatedAt )" +
            "from Category  category where  (category.deleted = true)  and category.updatedAt between  :start and :end group by  DATE_FORMAT(category.updatedAt, 'yyyy-MM-dd HH:mm:ss') ")
    List<CountStats> deletedStats(@Param("start") Date start, @Param("end") Date end);
}
