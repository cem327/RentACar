package com.rentacar.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {

    private final String SECRETKEY = System.getenv("SECRET_KEY");
    private final String ISSUER = "OWNER"; // HACK
    private final Long EXDATE = 1000L * 40; // TOKEN VALIDATION TIME.


    /**
     * This will be used to create a token.
     * @param authId The authentication ID for which the token will be generated.
     *  *               This ID uniquely identifies the user or entity requesting the token
     * @return An optional containing the generated token if successful, or an empty optional
     *         if the token generation fails.
     */
    public Optional<String> createToken(Long authId){
        String token;
        try{
            token = JWT.create()
                    .withAudience()
                    .withClaim("authid", authId)
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis()+EXDATE))
                    .sign(Algorithm.HMAC512(SECRETKEY));
            return Optional.of(token);
        }catch (Exception e){
            return Optional.empty(); // TODO : EXCEPTION HANDLING FOR AUTH CLASS
        }
    }

    /**
     * Token will be validated here.
     * @param token The token that will be validated.
     * @return Optional of Auth ID.
     */
    public Optional<Long> validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT == null)
                return Optional.empty();
            Long authId = decodedJWT.getClaim("authid").asLong();
            return Optional.of(authId);
        }catch (Exception e){
            return Optional.empty();
        }
    }
}
