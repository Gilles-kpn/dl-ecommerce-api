package fr.gilles.dleapi.services.user;

import fr.gilles.dleapi.entities.roles.Privilege;
import fr.gilles.dleapi.entities.roles.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
@Component
public class AuthUserService  implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        fr.gilles.dleapi.entities.user.User user = userService.findByEmail(username);
        if(user != null)
            return new User( user.getEmail(),
                    user.getPassword(),
                    user.isEnabled(),
                    true,
                    true,
                    !user.isDeleted(),
                    getAuthorities(user.getRoles()) );
        return  null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }



    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();

        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
