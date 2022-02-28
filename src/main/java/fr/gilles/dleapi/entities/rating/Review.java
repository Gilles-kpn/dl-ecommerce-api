package fr.gilles.dleapi.entities.rating;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.entities.audit.Audit;
import fr.gilles.dleapi.entities.products.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class Review extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;


    @ManyToOne
    @NotNull
    @JsonIgnore
    private Product product;

    @ManyToOne
    @NotNull
    private User user;
}
