package fr.gilles.auth.services.user;


import fr.gilles.auth.entities.User;
import fr.gilles.auth.entities.roles.Privilege;
import fr.gilles.auth.repositories.RoleRepository;
import fr.gilles.auth.repositories.UserRepository;
import fr.gilles.auth.services.role.PrivilegeService;
import fr.gilles.auth.services.role.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private  final RoleService roleService;
    private final PrivilegeService privilegeService;


    public User findByEmail(@NotNull @NotEmpty String email){
        return  userRepository.findByEmail(email);
    }



    public User create(@NotNull User user) throws Exception {
        if (findByEmail(user.getEmail()) == null){
            if(user.getRoles()==null){
                Privilege readPrivilege  = privilegeService.createPrivilegeIfNotFound("READ_PRIVILEGE");
                roleService.createRoleIfNotFound("ROLE_USER", List.of(readPrivilege));
                user.setRoles(List.of(roleService.createRoleIfNotFound("ROLE_USER", List.of(readPrivilege))));
            }
            return  userRepository.save(user);
        }else throw new Exception("There is an account with that email address: " + user.getEmail());
    }


    public void activate(@NotNull String email) throws NotFoundException{
        User user = findByEmail(email);
        if(user != null){
            user.setEnabled(true);
            userRepository.save(user);
        }else{
            throw new NotFoundException("Cannot active not found account");
        }
    }


    public void update(User user){
        user = findByEmail(user.getEmail());
        if(user != null){
            userRepository.save(user);
        }else{
            throw new NotFoundException("Not Found account");
        }
    }






}
