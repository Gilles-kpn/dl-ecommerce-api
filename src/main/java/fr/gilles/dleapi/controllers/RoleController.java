package fr.gilles.dleapi.controllers;

import fr.gilles.dleapi.entities.roles.Role;
import fr.gilles.dleapi.services.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Token")
@RequestMapping("role")
@Tag(name = "Roles", description = "All endpoints about role")
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "GET Available Roles | MANAGER  ROLE REQUIRED")
    public ResponseEntity<List<Role>> all(){
        return  ResponseEntity.ok(roleService.all());
    }



}
