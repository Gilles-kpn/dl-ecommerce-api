package fr.gilles.dleapi.services.role;

import fr.gilles.dleapi.entities.roles.Privilege;
import fr.gilles.dleapi.repositories.PrivilegeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;



    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    public Privilege findByName(String name){
        return  privilegeRepository.findByName(name);
    }
}
