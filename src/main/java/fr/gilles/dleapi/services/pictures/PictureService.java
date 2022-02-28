package fr.gilles.dleapi.services.pictures;

import fr.gilles.dleapi.entities.picture.Picture;
import fr.gilles.dleapi.repositories.PictureRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Getter
@Setter
@AllArgsConstructor
public class PictureService {
    private final PictureRepository pictureRepository;


    public Optional<Picture> findByCode(String code) {
        return pictureRepository.findByCode(code);
    }


    public void delete(Picture picture) {
        picture.setDeleted(true);
        pictureRepository.save(picture);
    }
}
