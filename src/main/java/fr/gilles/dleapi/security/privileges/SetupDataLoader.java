package fr.gilles.dleapi.security.privileges;

import fr.gilles.dleapi.entities.user.Admin;
import fr.gilles.dleapi.entities.roles.Privilege;
import fr.gilles.dleapi.entities.roles.Role;
import fr.gilles.dleapi.services.role.PrivilegeService;
import fr.gilles.dleapi.services.role.RoleService;
import fr.gilles.dleapi.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class SetupDataLoader  implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;
    private final PrivilegeService privilegeService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Privilege readPrivilege  = privilegeService.createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = privilegeService.createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege allPrivilege = privilegeService.createPrivilegeIfNotFound("ALL_PRIVILEGES");


        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        List<Privilege> managerPrivileges = Arrays.asList(readPrivilege, writePrivilege, allPrivilege);
        roleService.createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        roleService.createRoleIfNotFound("ROLE_MANAGER", managerPrivileges);
        roleService.createRoleIfNotFound("ROLE_USER", List.of(readPrivilege));

        Role managerRole = roleService.findByName("ROLE_MANAGER");
        Admin user = new Admin();
        user.setPassword(passwordEncoder.encode("Gkpanou2@gmail.ce"));
        user.setEmail("admin@admin.com");
        user.setName("KPANOU Gilles");
        user.setRoles(List.of(managerRole));
        user.setEnabled(true);
        try {
            userService.create(user);
        } catch (Exception ignored) {

        }

    }


}
