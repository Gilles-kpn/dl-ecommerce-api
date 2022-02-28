package fr.gilles.dleapi.entities.namingstrategy;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Custom naming strategie for auth module
 */
public class NamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    private static final String PREFIX = "dl_ecommerce_";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalTableName(new Identifier(PREFIX + name.getText(),name.isQuoted()), jdbcEnvironment);
    }

}
