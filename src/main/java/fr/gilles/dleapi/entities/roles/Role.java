package fr.gilles.dleapi.entities.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Proxy(lazy = false)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    private String name;

    @ManyToMany(mappedBy = "roles" )
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    private Collection<User> users;

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;
}

