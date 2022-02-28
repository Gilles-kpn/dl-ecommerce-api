package fr.gilles.dleapi.services.tracking;

import fr.gilles.dleapi.entities.tracking.History;
import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.repositories.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HistoryService<U> {
    private final HistoryRepository<U> historyRepository;


    public Page<History> findByUser(User user, Pageable pageable){
        return  historyRepository.findByUser(user,pageable);
    }


    private History add(History history){
        return  historyRepository.save(history);
    }

    public Page<History> all(Pageable pageable){
        return  historyRepository.findAllBy(pageable);
    }


    public History create(User user, String action , U related){
        History history = new History();
        history.setAction(action);
        history.setRelated(related.toString());
        history.setUser(user);
        return  add(history);
    }
}
