package fr.gilles.auth.services.history;

import fr.gilles.auth.entities.History;
import fr.gilles.auth.entities.User;
import fr.gilles.auth.repositories.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HistoryService<U> {
    public final HistoryRepository<U> historyRepository;


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
