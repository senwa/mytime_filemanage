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
                mapToGrantedAuthorities(new ArrayList<String>())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
    	List<GrantedAuthority> res = null;
    	if(authorities!=null&&!authorities.isEmpty()){
    		res = new ArrayList<GrantedAuthority>();
    		for(int i=0; i<authorities.size(); i++){
    			res.add(new SimpleGrantedAuthority(authorities.get(i)));
    		}
    	}
    	return res;
    }
}
