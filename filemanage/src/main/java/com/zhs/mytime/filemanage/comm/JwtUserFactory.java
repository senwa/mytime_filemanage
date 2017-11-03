package com.zhs.mytime.filemanage.comm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.zhs.mytime.filemanage.model.User;
import com.zhs.mytime.filemanage.security.JwtUser;

public class JwtUserFactory {
	 private JwtUserFactory() {
	 
	 }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUnionId(),
                user.getAccount(),
                user.getFullname(),
                user.getPwd(),
                user.getEmail(),
                user.getPhone(),
                user.getLastPasswordResetDate(),
                user.getState(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(String roles) {
    	List<GrantedAuthority> res = null;
    	if(StringUtils.isNotEmpty(roles)){
    		res = new ArrayList<GrantedAuthority>();
    		String[] roleArray = roles.split(",");
    		for(int i=0; i<roleArray.length; i++){
    			res.add(new SimpleGrantedAuthority(roleArray[i]));
    		}
    	}
    	return res;
    }
}
