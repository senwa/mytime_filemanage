package com.zhs.mytime.filemanage.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	 	@Autowired
	    private UserDetailsService userDetailsService;

	    @Autowired
	    private JwtTokenUtil jwtTokenUtil;

	    @Value("${jwt.header}")
	    private String tokenHeader;

	    @Value("${jwt.tokenHead}")
	    private String tokenHead;

	    @Override
	    protected void doFilterInternal(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            FilterChain chain) throws ServletException, IOException {
	        String authHeader = request.getHeader(this.tokenHeader);
	        if (authHeader != null && authHeader.startsWith(tokenHead)) {
	             final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
	             String account= jwtTokenUtil.getUsernameFromToken(authToken);
	        	 logger.info("checking authentication: " + account);
	        	 
	        	 if (account != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	
	                UserDetails userDetails = this.userDetailsService.loadUserByUsername(account);
	
	                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
	                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                            userDetails, null, userDetails.getAuthorities());
	                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
	                            request));
	                    logger.info("authenticated user " + account + ", setting security context");
	                    SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
	            }
	        }

	        chain.doFilter(request, response);
	    }
}
