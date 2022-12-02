package pt.com.renan.javabechallenge.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pt.com.renan.javabechallenge.MainApplication;
import pt.com.renan.javabechallenge.domain.entity.User;

@Service
public class JwtService {
	
	
	@Value("${security.jwt.expiration}")
	private String expiration;
	
	@Value("${security.jwt.signature-key}")
	private String signatureKey;
	
	
	
	public String generateToken(User user) {
		long expString = Long.valueOf(expiration);
		LocalDateTime dateHourExpiration = LocalDateTime.now().plusMinutes(expString);
		Instant instant = dateHourExpiration.atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		return Jwts
				.builder()
				.setSubject(user.getLogin())
				.setExpiration(date)
				.signWith(SignatureAlgorithm.HS512, signatureKey)
				.compact();
	}
	
	private Claims getClaims(String token) throws ExpiredJwtException {
		return Jwts
				.parser()
				.setSigningKey(signatureKey)
				.parseClaimsJws(token)
				.getBody();
	}
	 
	public boolean isTokenValid(String token) {
		try {
			 Claims claims = getClaims(token);
			 Date expirationDate = claims.getExpiration();
			 LocalDateTime date = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			 return LocalDateTime.now().isBefore(date);
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getUserLogin(String token) throws ExpiredJwtException {
		return (String) getClaims(token).getSubject();
	}
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class);
		JwtService service = context.getBean(JwtService.class);
		User user = User.builder().login("dude").build();
		String token = service.generateToken(user);
		System.out.println(token);
		
		boolean isValidToken = service.isTokenValid(token);
		System.out.println("Is token valid ? " + isValidToken);
		
		System.out.println(service.getUserLogin(token));
	}
}
