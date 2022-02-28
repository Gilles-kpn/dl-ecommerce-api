package fr.gilles.dleapi.payloader.user;

import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.payloader.Payloader;
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
