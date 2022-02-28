package fr.gilles.dleapi.repositories;

import fr.gilles.dleapi.entities.roles.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    Privilege findByName(String name);
}
