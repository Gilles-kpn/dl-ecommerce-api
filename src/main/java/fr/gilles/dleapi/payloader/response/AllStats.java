package fr.gilles.dleapi.payloader.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllStats {
    private int productCount;
    private int userCount;
    private int categoryCount;
    private int deletedProductCount;
    private int enabledUserCount;
    private int deletedCategoryCount;

}