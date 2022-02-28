package fr.gilles.dleapi.payloader.user;

import fr.gilles.dleapi.entities.user.Admin;
import fr.gilles.dleapi.entities.user.Client;
import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.payloader.Payloader;
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

    public Client toClient(){
        Client client = new Client();
        client.setName(name);
        client.setPassword(password);
        client.setEmail(email);
        return  client;
    }


    public Admin toAdmin(){
        Admin admin = new Admin();
        admin.setName(name);
        admin.setPassword(password);
        admin.setEmail(email);
        return  admin;
    }

}
