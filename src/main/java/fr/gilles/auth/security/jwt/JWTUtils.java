package fr.gilles.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JWTUtils {

    private final long validityTime = 3600 * 1000 * 8;
    public static final String PREFIX = "Bearer ";
    private final String secretKey ="Key";

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        return JWTUtils.PREFIX+ Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt( new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+validityTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token){
        return  extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token){
        return  extractExpirationDate(token).before(new Date());
    }

    public boolean isValidToken(String token, UserDetails userDetails){
        return  userDetails.getUsername().equals(extractUserName(token)) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        return  claimsTFunction.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token){
        return  Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
    }

}
