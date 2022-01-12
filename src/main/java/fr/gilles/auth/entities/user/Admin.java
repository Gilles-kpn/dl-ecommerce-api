package fr.gilles.auth.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.auth.entities.products.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Getter
@Setter
public class Admin extends User{

    @OneToMany
    @JsonIgnore
    private Collection<Product> products;
}
