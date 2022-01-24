package fr.gilles.auth.services.user;


import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.entities.roles.Privilege;
import fr.gilles.auth.payloader.query.QueryParams;
import fr.gilles.auth.payloader.response.CountStats;
import fr.gilles.auth.repositories.UserRepository;
import fr.gilles.auth.services.role.PrivilegeService;
import fr.gilles.auth.services.role.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
        User user1 = findByEmail(user.getEmail());
        if (user1 == null){
            if(user.getRoles()==null){
                Privilege readPrivilege  = privilegeService.createPrivilegeIfNotFound("READ_PRIVILEGE");
                roleService.createRoleIfNotFound("ROLE_USER", List.of(readPrivilege));
                user.setRoles(List.of(roleService.createRoleIfNotFound("ROLE_USER", List.of(readPrivilege))));
            }
            return  userRepository.save(user);
        }else{
            if(user1.isDeleted()){
                user1.setName(user.getName());
                user1.setEnabled(false);
                user1.setDeleted(false);
                return  userRepository.save(user);

            }else  throw new Exception("There is an account with that email address: " + user.getEmail());
        }
    }


    public void activate(@NotNull @NotEmpty  String email) throws NotFoundException{
        User user = findByEmail(email);
        if(user != null){
            user.setEnabled(true);
            userRepository.save(user);
        }else{
            throw new NotFoundException("Cannot active not found account");
        }
    }


    public void update(@NotNull @Validated User user){
        user = findByEmail(user.getEmail());
        if(user != null){
            userRepository.save(user);
        }else{
            throw new NotFoundException("Not Found account");
        }
    }

    public Page<User> all(QueryParams queryParams){
        return  userRepository.findAllByDeleted(queryParams.toPageRequest(), queryParams.isDeleted());
    }

    public void delete(@NotNull @NotEmpty  String email) throws Exception {
        User user = findByEmail(email);
        if(user != null)
        {
            user.setDeleted(true);
            user.setEnabled(false);
            userRepository.save(user);
        }else throw new Exception("User not found");

    }


    public void recycle(@NotNull @NotEmpty String email) throws Exception {
        User user = findByEmail(email);
        if (user != null){
            user.setDeleted(false);
            userRepository.save(user);
        }else
            throw  new Exception("Not Found");
    }

    public int count(boolean enabled){
        return  userRepository.countAllByDeletedAndEnabled(false, enabled);
    }

    public int count(){
        return (int) userRepository.count();
    }

    public List<CountStats> createdStats(Date start, Date end){
        return  userRepository.createdStats(start, end);
    }

    public List<CountStats> deletedStats(Date start, Date end){
        return  userRepository.deletedStats(start, end);
    }




}
