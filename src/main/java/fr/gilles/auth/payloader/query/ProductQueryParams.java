package fr.gilles.auth.payloader.query;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.validation.constraints.Min;

@Getter
@Setter
public class ProductQueryParams extends QueryParams{

    private  String[] categories = {};

    @Min(0)
    private int priceMin;

    @Min(0)
    private int priceMax;



}
