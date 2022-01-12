package fr.gilles.auth.controllers;

import fr.gilles.auth.entities.user.Admin;
import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.entities.roles.Role;
import fr.gilles.auth.payloader.query.QueryParams;
import fr.gilles.auth.payloader.user.UserCreate;
import fr.gilles.auth.services.role.RoleService;
import fr.gilles.auth.services.user.UserService;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find user", e);
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




}
