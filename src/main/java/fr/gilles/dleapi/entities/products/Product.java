package fr.gilles.dleapi.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.audit.Audit;
import fr.gilles.dleapi.entities.picture.Picture;
import fr.gilles.dleapi.entities.rating.Like;
import fr.gilles.dleapi.entities.rating.Review;
import fr.gilles.dleapi.entities.user.Admin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collection;

@Entity
@ToString
@Getter
@Setter
public class Product extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private int id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @Min(value = 1)
    private int price;

    @OneToMany
    @NotNull
    @Size(min = 2, max = 5)
    @Valid
    @ToString.Exclude
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Collection<@NotNull  Picture> pictures;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Category category;


    @OneToMany
    @ToString.Exclude
    @JsonIgnore
    private Collection<@NotNull Like> likes;


    @OneToMany
    @ToString.Exclude
    @JsonIgnore
    private Collection<@NotNull Review> reviews;


    private boolean available = false;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PROCESSING;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    @NotNull
    @NotFound(action = NotFoundAction.IGNORE)
    private Admin author;


    private int quantity = 0;

}
