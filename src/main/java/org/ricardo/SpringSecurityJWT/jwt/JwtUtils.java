package org.ricardo.SpringSecurityJWT.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

	@Value("${jwt.secret.key}")
	private String secretKey;
	@Value("${jwt.time.expiration}")
	private String timeExpiration;
	
	// Generate token
	public String generateAccesToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	// To valid 
	public boolean isTokenValid(String token) {
		try {
			Jwts.parser()
			.setSigningKey(getSignatureKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
			return true;
		} catch (Exception e) {
			log.error("Token no valido, error: ".concat(e.getMessage()));
			return false;
		}
	}
	
	// Get all claims
	public Claims extracAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(getSignatureKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	// Get username from token
	public String getUsernameFromToken(String token) {
		return getClaim(token, Claims::getSubject);
	}
	
	// Get just one claim
	public <T> T getClaim(String token, Function<Claims, T> claimsFuntion) {
		Claims claims = extracAllClaims(token);
		return claimsFuntion.apply(claims);
	}
	
	// Get sign of method
	public Key getSignatureKey() {
		byte [] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
