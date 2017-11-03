package com.zhs.mytime.filemanage.security;

import java.io.Serializable;

public class  JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String account;
	private String pwd;
    
    
    public JwtAuthenticationRequest(String account, String pwd) {
        this.setAccount(account);
        this.setPwd(pwd);
    }
    
    public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

    public JwtAuthenticationRequest() {
        super();
    }

}
