package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.payloader.response.CountStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Page<User> findByEmailContainingAndDeleted(String email,  boolean deleted,Pageable pageable);
    Page<User> findAllByDeleted(Pageable pageable, boolean deleted);
    int countAllByDeletedAndEnabled(boolean deleted, boolean enabled);

    @Query("select  " +
            "new fr.gilles.dleapi.payloader.response.CountStats(" +
            "count(user.code)," +
            "to_date(cast(user.createdAt as text), 'yyyy-MM-dd') )" +
            "from User  user  " +
            "where  "+
            "user.createdAt " +
            "between to_date(cast(:start as text), 'yyyy-MM-dd') " +
            "and to_date(cast( :end as text), 'yyyy-MM-dd') " +
            "group by  " +
            "to_date(cast(user.createdAt as text), 'yyyy-MM-dd') ")
    List<CountStats> createdStats(@Param("start") Date start, @Param("end") Date end);

    @Query("select  " +
            "new fr.gilles.dleapi.payloader.response.CountStats(count(user.code)," +
            "to_date(cast(user.updatedAt as text), 'yyyy-MM-dd') )" +
            "from User  user  " +
            "where  (user.deleted = true )" +
            "and " +
            "user.updatedAt " +
            "between to_date(cast(:start as text), 'yyyy-MM-dd') " +
            "and to_date(cast( :end as text), 'yyyy-MM-dd') " +
            "group by  " +
            "to_date(cast(user.updatedAt as text), 'yyyy-MM-dd') ")
    List<CountStats> deletedStats(@Param("start") Date start, @Param("end") Date end);
}
