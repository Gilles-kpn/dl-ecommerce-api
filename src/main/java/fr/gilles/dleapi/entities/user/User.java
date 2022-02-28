package fr.gilles.dleapi.entities.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.audit.Audit;
import fr.gilles.dleapi.entities.rating.Like;
import fr.gilles.dleapi.entities.roles.Role;
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


    private String picture;

    private boolean enabled = false;

    private String provider;

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
        if (o ==null) return  false;
        if (this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return super.equals(o) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @OneToMany
    @JsonIgnore
    @ToString.Exclude
    private Collection<Like> likedProducts;

}
