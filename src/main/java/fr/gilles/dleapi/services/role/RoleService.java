package fr.gilles.dleapi.services.role;

import fr.gilles.dleapi.entities.roles.Privilege;
import fr.gilles.dleapi.entities.roles.Role;
import fr.gilles.dleapi.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }




    public List<Role> all(){
        return roleRepository.findAll();
    }

    public Role findByName(String name){
        return  roleRepository.findByName(name);
    }
}
