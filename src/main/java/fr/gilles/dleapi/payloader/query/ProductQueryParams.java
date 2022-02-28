package fr.gilles.dleapi.payloader.query;

import fr.gilles.dleapi.entities.products.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class ProductQueryParams extends QueryParams{

    private  String[] categories = {};

    @Min(0)
    private int priceMin;

    @Min(0)
    private int priceMax;

    private TransactionStatus status = TransactionStatus.SUCCESS;



}
