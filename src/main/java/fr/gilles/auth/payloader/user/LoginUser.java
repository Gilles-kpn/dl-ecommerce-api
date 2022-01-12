package fr.gilles.auth.payloader.user;

import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.payloader.Payloader;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginUser extends Payloader<User> {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @Override
    public User toModel() {
        User user = new User();
        user.setPassword(password);
        return null;
    }
}
