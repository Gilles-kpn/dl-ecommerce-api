package fr.gilles.auth.repositories;

import fr.gilles.auth.entities.tracking.History;
import fr.gilles.auth.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;

public interface HistoryRepository<U> extends JpaRepository<History, Integer> {
    Page<History> findByUser(@NotNull User user, Pageable pageable);
    Page<History> findAllBy(Pageable pageable);

}