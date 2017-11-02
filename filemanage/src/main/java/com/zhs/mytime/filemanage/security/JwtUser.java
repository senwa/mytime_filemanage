package com.zhs.mytime.filemanage.security;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import net.minidev.json.annotate.JsonIgnore;

public class JwtUser implements UserDetails {

	private final String id;
	private final String unionId;
	private final String account;
    private final String fullName;
    private final String pwd;
    private final String email;
    private final String phone;
    private final int state;//后期根据state区分账号状态
    private final Date lastPasswordReset;
    private final Collection<? extends GrantedAuthority> authorities;
	
    public JwtUser(
            String id,
            String unionId,
            String account,
            String fullName,
            String pwd,
            String email,
            String phone,
            Date lastPasswordReset,
            Byte state,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.fullName = fullName;
        this.pwd = pwd;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.unionId  = unionId;
        this.account = account;
        this.authorities = authorities;
        this.lastPasswordReset = lastPasswordReset;
    }
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return null;
	}
	
	@JsonIgnore
    public String getId() {
        return id;
    }
	
	@JsonIgnore
	@Override
	public String getPassword() {
		
		return this.pwd;
	}
	@JsonIgnore
	@Override
	public String getUsername() {
		return this.account;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {

		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
	
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {

		return true;
	}
	
	 @JsonIgnore
	public Date getLastPasswordResetDate() {
	        return lastPasswordReset;
	}

}
