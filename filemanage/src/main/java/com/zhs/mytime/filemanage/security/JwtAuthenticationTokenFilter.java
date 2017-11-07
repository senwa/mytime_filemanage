package com.zhs.mytime.filemanage.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import com.zhs.mytime.filemanage.comm.RedisUtil;

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
	                    //把信息存入redis中
	                    JwtUser jwtUser = (JwtUser) userDetails;
	                    Map<String,String> user = new HashMap<String,String>();
	                    user.put("id", jwtUser.getId());
	                    user.put("account", jwtUser.getUsername());
	                    user.put("fullName", jwtUser.getFullName());
	                    //map存入redis
	                    RedisUtil.addMap(authToken, RedisUtil.EXRP_DAY,user);//后期访问直接从redis中取
	                    
	                }else{
	                	//需要重新登录
	                }
	            }
	        }

	        chain.doFilter(request, response);
	    }
}
