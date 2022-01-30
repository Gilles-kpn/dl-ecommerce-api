package fr.gilles.auth.repositories;

import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.payloader.response.CountStats;
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
    Page<User> findAllByDeleted(Pageable pageable, boolean deleted);
    int countAllByDeletedAndEnabled(boolean deleted, boolean enabled);

    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(count(user.code),user.createdAt )" +
            "from User  user  where user.createdAt between  :start and :end group by  to_date(cast(user.createdAt as text), 'yyyy-MM-dd HH:mm:ss')")
    List<CountStats> createdStats(@Param("start") Date start, @Param("end") Date end);

    @Query("select  " +
            "new fr.gilles.auth.payloader.response.CountStats(count(user.code),user.updatedAt )" +
            "from User  user  where  (user.deleted = true ) and user.updatedAt between  :start and :end group by  to_date(cast(user.updatedAt as text), 'yyyy-MM-dd HH:mm:ss') ")
    List<CountStats> deletedStats(@Param("start") Date start, @Param("end") Date end);
}
