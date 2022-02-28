package fr.gilles.dleapi.payloader.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GraphStats {
    private List<CountStats> userCreatedStats;
    private List<CountStats> userDeletedStats;
    private List<CountStats> productCreatedStats;
    private List<CountStats> productDeletedStats;
    private List<CountStats> categoryCreatedStats;
    private List<CountStats> categoryDeletedStats;
}
