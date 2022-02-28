package fr.gilles.dleapi.security.jwt;

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

    private static final long VALIDITY_TIME = 3600 * 1000 * 8L;
    public static final String PREFIX = "Bearer ";
    private static final String SECRET_KEY ="Key";

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        return JWTUtils.PREFIX+ Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt( new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ VALIDITY_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
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
        return  Jwts.parser().setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
    }

}
