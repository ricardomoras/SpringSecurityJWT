package org.ricardo.SpringSecurityJWT.security.filters;

import java.io.IOException;

import org.ricardo.SpringSecurityJWT.jwt.JwtUtils;
import org.ricardo.SpringSecurityJWT.servers.UserDetailsServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServerImpl userDetailsServerImpl;

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain) throws ServletException, IOException {

		String tokenHeader = request.getHeader("Authorization");
		logger.info("Processing token: {}", tokenHeader);
		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			String token = tokenHeader.substring(7);
			
			if (jwtUtils.isTokenValid(token)) {
				String username = jwtUtils.getUsernameFromToken(token);
				UserDetails userDetails = userDetailsServerImpl.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}

		}
		filterChain.doFilter(request, response);
	}

}
