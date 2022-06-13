package com.app.fileprocess.security.service;

public interface SecurityService {
	String findLoggedInUsername();

    void autoLogin(String username, String password);
}
