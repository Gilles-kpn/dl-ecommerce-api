package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
