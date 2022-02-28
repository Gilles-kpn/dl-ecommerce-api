package fr.gilles.dleapi.entities.picture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.audit.Audit;
import fr.gilles.dleapi.entities.products.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@ToString
@Getter
@Setter
@Where(clause = "deleted = false")
public class Picture extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private int id;

    @NotNull
    @NotBlank
    private String url ;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private Product product;

    private String publicId;

    private float version;

    private String signature;

    private float width;

    private float height;

    private String format;

    private String resourceType;


    @Override
    public boolean equals(Object o) {
        return  super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
