package fr.gilles.auth.controllers;

import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.user.Admin;
import fr.gilles.auth.payloader.response.AllStats;
import fr.gilles.auth.payloader.response.CurrentAdminStats;
import fr.gilles.auth.payloader.response.CountStats;
import fr.gilles.auth.payloader.response.GraphStats;
import fr.gilles.auth.services.product.CategoryService;
import fr.gilles.auth.services.product.ProductService;
import fr.gilles.auth.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("statistic")
@Tag(name = "Statistics", description = "All endpoints about product")
@AllArgsConstructor

public class StatisticController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;


    @GetMapping("current/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get current admin stats | ADMIN OR MANAGER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<CurrentAdminStats> currentAdminStatistic(Authentication authentication){
        Admin admin = (Admin) userService.findByEmail(authentication.getName());
        if (admin != null){
            CurrentAdminStats currentAdminStats = new CurrentAdminStats();
            for (Product p:admin.getProducts()) {
                if (!p.isDeleted()){
                    currentAdminStats.setProductsCount(currentAdminStats.getProductsCount() +1);
                    currentAdminStats.setLikesCount(p.getLikes().size() + currentAdminStats.getLikesCount());
                }
            }
            return  ResponseEntity.ok(currentAdminStats);
        }else {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot find current admin");
        }

    }

    @GetMapping("all")
    @PreAuthorize("hasAnyRole( 'ROLE_MANAGER')")
    @Operation(summary = "Get All Stats| MANAGER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<AllStats> stats(){
        AllStats allStats = new AllStats();
        allStats.setProductCount(productService.count(false));
        allStats.setCategoryCount(categoryService.count(false));
        allStats.setUserCount(userService.count());
        allStats.setDeletedProductCount(productService.count(true));
        allStats.setDeletedCategoryCount(categoryService.count(true));
        allStats.setEnabledUserCount(userService.count(true));
        return  ResponseEntity.ok(allStats);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole( 'ROLE_MANAGER')")
    @Operation(summary = "Get all Stats | MANAGER ROLE REQUIRED", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<GraphStats> userCreatedStats(String start, String end)  {
        try{
            GraphStats graphStats = new GraphStats();
            graphStats.setUserCreatedStats(userService.createdStats(format(start),format(end)));
            graphStats.setUserDeletedStats(userService.deletedStats(format(start),format(end)));
            graphStats.setProductCreatedStats(productService.createdStats(format(start),format(end)));
            graphStats.setProductDeletedStats(productService.deletedStats(format(start),format(end)));
            graphStats.setCategoryCreatedStats(categoryService.createdStats(format(start),format(end)));
            graphStats.setCategoryDeletedStats(categoryService.deletedStats(format(start),format(end)));
            return  ResponseEntity.ok(graphStats);
        }catch (ParseException parseException){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,parseException.getMessage() ,parseException);
        }

    }

    private Date format(String date) throws ParseException {
        return  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
    }



}
