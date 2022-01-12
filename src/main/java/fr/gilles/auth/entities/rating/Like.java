package fr.gilles.auth.entities.rating;

import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.entities.audit.Audit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class Like extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @NotNull
    @ManyToOne
    private Product product;

    @NotNull
    @ManyToOne
    private User user;
}