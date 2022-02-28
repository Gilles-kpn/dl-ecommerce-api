package fr.gilles.dleapi.controllers;

import fr.gilles.dleapi.entities.user.Admin;
import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.entities.roles.Role;
import fr.gilles.dleapi.payloader.query.QueryParams;
import fr.gilles.dleapi.payloader.response.ExceptionMessage;
import fr.gilles.dleapi.payloader.user.UserCreate;
import fr.gilles.dleapi.services.role.RoleService;
import fr.gilles.dleapi.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("user")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Token")
@Tag(name = "User", description = "All endpoints about user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @Operation(summary = "select all user | MANAGER OR ADMIN ROLE REQUIRED")
    public ResponseEntity<Page<User>> all(QueryParams queryParams){
        return  ResponseEntity.ok(userService.all(queryParams));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Create user | | MANAGER  ROLE REQUIRED")
    public ResponseEntity<User> create(@Validated @RequestBody UserCreate userCreate){
        Role role = roleService.findByName("ROLE_ADMIN");
        Admin user = userCreate.toAdmin();
        user.setRoles(List.of(role));
        try{
            return  ResponseEntity.ok(userService.create(user));
        }catch (Exception e) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "Email already register", e);
        }

    }


    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("{email}/delete")
    @Operation(summary = "Delete user by email | | MANAGER ROLE REQUIRED")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @NotEmpty String email){
        try {
            userService.delete(email);
            return  ResponseEntity.ok(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND, e);
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PutMapping("{email}/recycle")
    @Operation(summary = "Recycle deleted user | MANAGER  ROLE REQUIRED")
    public ResponseEntity<String> recycle(@PathVariable @NotNull @NotEmpty  String email){
        try {
            userService.recycle(email);
            return  ResponseEntity.ok("User "+email+" has been recycled");
        } catch (Exception e) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot recycle existent user");
        }
    }

    @GetMapping("{email}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @Operation(summary = "select user by email | MANAGER OR ADMIN ROLE REQUIRED")
    public ResponseEntity<User> findByEmail(@PathVariable @NotNull @NotEmpty String email){
        try {
            return  ResponseEntity.ok(userService.findByEmail(email));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND, e);
        }
    }

    @GetMapping("{email}/roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @Operation(summary = "select user by email | MANAGER OR ADMIN ROLE REQUIRED")
    public ResponseEntity<Collection<Role>> findRolesByEmail(@PathVariable @NotNull @NotEmpty String email){
        try {
            return  ResponseEntity.ok(userService.findByEmail(email).getRoles());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND, e);
        }
    }




}