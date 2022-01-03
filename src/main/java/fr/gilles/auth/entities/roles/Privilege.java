package fr.gilles.auth.entities.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.auth.entities.audit.Audit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Privilege  extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    private String name;

    @ManyToMany(mappedBy = "privileges")
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private Collection<Role> roles;
}