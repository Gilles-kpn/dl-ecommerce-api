package fr.gilles.auth.controllers;

import fr.gilles.auth.entities.tracking.History;
import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.payloader.query.QueryParams;
import fr.gilles.auth.services.tracking.HistoryService;
import fr.gilles.auth.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Token")
@RequestMapping("history")
@Tag(name = "History", description = "All endpoints about history")
public class HistoryController {
    private final HistoryService<Object> historyService;
    private final UserService userService;


    @GetMapping
    @Operation(summary = "Current user history")
    public ResponseEntity<Page<History>> history(Authentication authentication, QueryParams queryParams){
        User user = userService.findByEmail(authentication.getName());
        return  ResponseEntity.ok(historyService.findByUser(user, queryParams.toPageRequest()));
    }

    @GetMapping("manager")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "All history  | MANAGER ROLE REQUIRED")
    public ResponseEntity<Page<History>> all(QueryParams queryParams){
        return  ResponseEntity.ok(historyService.all(queryParams.toPageRequest()));
    }


}
