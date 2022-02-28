package fr.gilles.dleapi.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.audit.Audit;
import fr.gilles.dleapi.entities.user.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@ToString
@Entity
public class Cart extends Audit {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @ToString.Exclude
    private int id;

    @OneToOne()
    private Client client;


    @JoinColumn
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<CardProduct> products;

}
