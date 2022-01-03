package fr.gilles.auth.payloader.user;

import fr.gilles.auth.payloader.Payloader;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangePassword {

    @NotEmpty
    @NotNull
    private String oldPassword;

    @NotEmpty
    @NotNull
    private String newPassword;
}
