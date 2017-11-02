package com.zhs.mytime.filemanage.service;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zhs.mytime.filemanage.comm.JwtUserFactory;
import com.zhs.mytime.filemanage.model.User;

public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UserMapperService userMapperService;
	
	@Override
	public UserDetails loadUserByUsername(String accountOrOtherInfo) throws UsernameNotFoundException {
		
		User user = userMapperService.getByAccountOrEmailOrPhoneOrUnionId(accountOrOtherInfo);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", accountOrOtherInfo));
        } else {
            return JwtUserFactory.create(user);
        }
	}
}
