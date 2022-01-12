package fr.gilles.auth.entities.picture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.auth.entities.audit.Audit;
import fr.gilles.auth.entities.products.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@ToString
@Getter
@Setter
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

    private String public_id;

    private float version;

    private String signature;

    private float width;

    private float height;

    private String format;

    private String resource_type;
}