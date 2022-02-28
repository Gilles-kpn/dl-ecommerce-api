package fr.gilles.dleapi.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.products.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Admin extends User{

    @OneToMany
    @JsonIgnore
    private Collection<Product> products;

    @Min(0)
    private float account = 0;
}
