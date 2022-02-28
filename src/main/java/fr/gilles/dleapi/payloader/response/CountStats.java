package fr.gilles.dleapi.payloader.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CountStats {
    private long count ;
    private Date date;
}
