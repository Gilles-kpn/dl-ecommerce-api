package fr.gilles.auth.controllers;

import fr.gilles.auth.entities.History;
import fr.gilles.auth.entities.User;
import fr.gilles.auth.entities.roles.Role;
import fr.gilles.auth.payloader.user.ChangePassword;
import fr.gilles.auth.payloader.user.LoginUser;
import fr.gilles.auth.payloader.user.UserCreate;
import fr.gilles.auth.security.jwt.JWTUtils;
import fr.gilles.auth.services.history.HistoryService;
import fr.gilles.auth.services.mail.EmailService;
import fr.gilles.auth.services.user.AuthUserService;
import fr.gilles.auth.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;


@RestController
@RequestMapping("auth")
@AllArgsConstructor
@Tag(name = "Auth", description = "All endpoints about authentication")
public class AuthController {
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AuthUserService authUserService;
    private final EmailService emailService;
    private final HistoryService<User> historyService;


    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new account")
    public ResponseEntity<String> register(@RequestBody @Validated UserCreate userToCreate){
        User user = userToCreate.toModel();
        user.setPassword(encoder.encode(user.getPassword()));
        try{
            userService.create(user);
            emailService.sendRegistrationEmail(user.getEmail(), "http://localhost:8080/auth/activate/"+jwtUtils.generateToken(authUserService.loadUserByUsername(userToCreate.getEmail())).replace(JWTUtils.PREFIX, ""));
            historyService.create(user,"Registration ", user);
            return  ResponseEntity.ok("User "+userToCreate.getEmail()+" created") ;
        }catch (Exception e){
            throw  new  ResponseStatusException(HttpStatus.CONFLICT,"Email "+userToCreate.getEmail()+" already register" , e);
        }

    }





    @PostMapping("login")
    @Operation(summary = "Login user")
    public ResponseEntity<String> login(@RequestBody @Validated LoginUser loginUser){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        User user = userService.findByEmail(loginUser.getEmail());
        historyService.create(user,"Connexion ", user);
        return  ResponseEntity.ok(jwtUtils.generateToken(authUserService.loadUserByUsername(loginUser.getEmail())));
    }


    @GetMapping("activate/{url}")
    @Operation(summary = "Activate user account ")
    public ResponseEntity<String> activate(@PathVariable @NotNull @NotEmpty @Validated String url){
        try{

            userService.activate(jwtUtils.extractUserName(url));
            User user = userService.findByEmail(jwtUtils.extractUserName(url));
            historyService.create(user,"Account activated ", user);
            return  ResponseEntity.ok("Account activated");
        }catch (Exception e){
            throw  new  ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found " , e);
        }
    }


    @GetMapping("current")
    @Operation(summary = "Get current user information", security = {@SecurityRequirement(name = "Bearer Token")})
    public ResponseEntity<User> current(Authentication authentication){
        User user = userService.findByEmail(authentication.getName());
        if (user == null)
            throw   new  ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found " );
        else
        return ResponseEntity.ok(user);

    }

    @GetMapping("current/role")
    @Operation(summary = "Get current user role", security = {@SecurityRequirement(name = "Bearer Token")})
    public ResponseEntity<Collection<Role>> currentRole(Authentication authentication){

        User user = userService.findByEmail(authentication.getName());
        if (user == null)
            throw   new  ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found " );
        else
            return ResponseEntity.ok(user.getRoles());
    }


    @PostMapping("current/password/change")
    @Operation(summary = "Change current user password", security = {@SecurityRequirement(name = "Bearer Token")})
    public ResponseEntity<String> changePassword(@Validated @RequestBody ChangePassword changePassword, Authentication authentication) throws MessagingException {
        User user = userService.findByEmail(authentication.getName());
        if (user == null)
            throw  new  ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found " );
        else{
            if (encoder.matches(changePassword.getOldPassword(), user.getPassword())){
                user.setPassword(encoder.encode(changePassword.getNewPassword()));
                userService.update(user);
                historyService.create(user,"Password Changed", user);
                emailService.sendPasswordChangeEmail(user.getEmail(), "http://localhost:8080/auth/password/reset");
                return  ResponseEntity.ok("Password Changed");
            }else
                throw new  ResponseStatusException(HttpStatus.UNAUTHORIZED,"Passwords not match" );
        }
    }







}
