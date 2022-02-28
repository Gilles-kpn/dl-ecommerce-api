package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.picture.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Integer> {

    Optional<Picture> findByCode(String code);
}
