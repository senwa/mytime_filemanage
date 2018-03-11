package com.zhs.mytime.filemanage.service;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zhs.mytime.filemanage.comm.UniqueIdUtil;
import com.zhs.mytime.filemanage.controller.AuthController;
import com.zhs.mytime.filemanage.dao.UserMapper;
import com.zhs.mytime.filemanage.model.User;
import com.zhs.mytime.filemanage.security.JwtTokenUtil;
import com.zhs.mytime.filemanage.security.JwtUser;

import static java.util.Arrays.asList;

@Service
public class AuthService {

	private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserMapper userRepository;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private static Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    public AuthService(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserMapper userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    public User register(User userToAdd) {
        final String username = userToAdd.getAccount();
        if(userRepository.getByAccountOrEmailOrPhoneOrUnionId(username)!=null) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPwd();
        userToAdd.setPwd(encoder.encode(rawPassword));
        userToAdd.setId(UniqueIdUtil.getGuidRan());
        userToAdd.setLastPasswordResetDate(new Date());
        userToAdd.setRegdate(new Date());
        userToAdd.setRoles("ROLE_USER");
        userToAdd.setRoleList(asList("ROLE_USER"));
        userRepository.insert(userToAdd);
        
        return userToAdd;
    }
    //重置密码
    public boolean resetPwd(User userToReset) {
    	if(userToReset!=null){
    		try{
    			User dbUser = userRepository.getByPhone(userToReset.getPhone());
        		logger.warn("修改密码:",dbUser.toString());
        		User userTemp = new User();
        		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        	    final String rawPassword = userToReset.getPwd();
        		userTemp.setPwd(encoder.encode(rawPassword));
        		userTemp.setId(dbUser.getId());
        		userTemp.setLastPasswordResetDate(new Date());
        		int upN = userRepository.updateByPrimaryKeySelective(userTemp);
        		logger.warn("成功修改{}条",upN);
    		}catch(Exception e){
    			e.printStackTrace();
    			logger.error(e.getMessage());
    			return false;
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
    
    public String generateToken(String username) {
    	 final String token = jwtTokenUtil.generateToken(username);        
         return token;
    }

    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);        
        return token;
    }

    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
        	//更新redis中记录的信息
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
