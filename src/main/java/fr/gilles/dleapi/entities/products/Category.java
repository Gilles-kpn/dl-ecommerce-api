package fr.gilles.dleapi.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.audit.Audit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@Where(clause = "deleted = false")
public class Category extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private int id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;


    private String description;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    @JsonIgnore
    private Collection<@NotNull Product> products;
}
