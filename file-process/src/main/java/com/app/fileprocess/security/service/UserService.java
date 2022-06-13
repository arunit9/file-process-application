package com.app.fileprocess.security.service;

import com.app.fileprocess.dao.entity.User;

public interface UserService {
	void save(User user);

    User findByUsername(String username);
}
