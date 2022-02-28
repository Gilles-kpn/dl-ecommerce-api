package fr.gilles.dleapi.entities.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.products.Cart;
import fr.gilles.dleapi.entities.products.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@Entity
@Getter
@Setter
public class Client extends User{

    @OneToMany
    @JsonIgnore
    private Set<Product> whishlist;

    @OneToOne(mappedBy = "client")
    private Cart cart;

    @Override
    public boolean equals(Object o) {
        return  super.equals(o);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
