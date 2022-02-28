package fr.gilles.dleapi.entities.tracking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.gilles.dleapi.entities.audit.Audit;
import fr.gilles.dleapi.entities.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class History extends Audit {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @NotNull
    private User user;

    @NotNull
    private String action;


    private String  related;




}
