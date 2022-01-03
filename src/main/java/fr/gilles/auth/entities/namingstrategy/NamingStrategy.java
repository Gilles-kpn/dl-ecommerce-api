package fr.gilles.auth.entities.namingstrategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

/**
 * Custom naming strategie for auth module
 */
public class NamingStrategy extends SpringPhysicalNamingStrategy {

    private final String PREFIX = "auth_";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return super.toPhysicalTableName(new Identifier(PREFIX + name.getText(),name.isQuoted()), jdbcEnvironment);
    }

}
