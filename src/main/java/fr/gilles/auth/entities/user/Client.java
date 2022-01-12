package fr.gilles.auth.entities.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.auth.entities.products.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
public class Client extends User{

    @OneToMany
    @JsonIgnore
    private Set<Product> whishlist;

    @Override
    public boolean equals(Object o) {
        return  super.equals(o);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
