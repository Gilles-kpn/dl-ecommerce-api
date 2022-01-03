package fr.gilles.auth.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.auth.entities.audit.Audit;
import fr.gilles.auth.entities.roles.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User extends Audit {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @ToString.Exclude
    private int id;

    private String name;

    private String email;

    @JsonIgnore
    @ToString.Exclude
    private String password;

    private boolean enabled = false;

    @ManyToMany
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Collection<Role> roles;

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return super.equals(o) && Objects.equals(email, user.email);
    }


}
