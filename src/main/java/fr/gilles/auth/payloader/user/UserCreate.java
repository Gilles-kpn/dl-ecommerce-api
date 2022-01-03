package fr.gilles.auth.payloader.user;

import fr.gilles.auth.entities.User;
import fr.gilles.auth.payloader.Payloader;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserCreate extends Payloader<User> {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String email;


    @Override
    public User toModel() {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }
}
