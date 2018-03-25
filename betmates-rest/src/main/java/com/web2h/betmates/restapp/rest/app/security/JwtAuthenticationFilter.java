package com.web2h.betmates.restapp.rest.app.security;

import static com.web2h.betmates.restapp.rest.app.security.SecurityConstants.EXPIRATION_TIME;
import static com.web2h.betmates.restapp.rest.app.security.SecurityConstants.HEADER_STRING;
import static com.web2h.betmates.restapp.rest.app.security.SecurityConstants.SECRET;
import static com.web2h.betmates.restapp.rest.app.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web2h.betmates.restapp.model.entity.user.AppUser;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			AppUser appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(appUser.getEmail(), appUser.getPassword(), new ArrayList<>());
			return authenticationManager.authenticate(token);
		} catch (IOException ioe) {
			// TODO Handle the exception a better way
			throw new RuntimeException(ioe);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		JwtBuilder jwtBuilder = Jwts.builder();
		jwtBuilder.setSubject(((User) authResult.getPrincipal()).getUsername());
		jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
		jwtBuilder.signWith(SignatureAlgorithm.HS512, SECRET.getBytes());
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwtBuilder.compact());
		
		String roles = "";
		for (GrantedAuthority authority : ((User) authResult.getPrincipal()).getAuthorities()) {
			if (!roles.isEmpty()) {
				roles += ",";
			}
			roles += authority.getAuthority();
		}
		jwtBuilder.claim("ROLES", roles);		
	}
}