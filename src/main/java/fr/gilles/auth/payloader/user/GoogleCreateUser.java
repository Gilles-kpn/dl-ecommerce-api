package fr.gilles.auth.payloader.user;

import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.payloader.Payloader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCreateUser extends Payloader<User> {

    @Override
    public User toModel() {
        return null;
    }
}
