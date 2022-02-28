package fr.gilles.dleapi.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.audit.Audit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Collection;

@Getter
@Setter
@Entity
public class CardProduct extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private int id;

    @ManyToMany
    private Collection<Cart> carts;

    @ManyToOne
    private Product product;

    @Min(1)
    private int quantity = 1;

}
