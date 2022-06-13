package com.app.fileprocess.security.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.fileprocess.dao.entity.User;
import com.app.fileprocess.dao.repository.RoleJpaRepository;
import com.app.fileprocess.dao.repository.UserJpaRepository;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
    private UserJpaRepository userRepository;
    @Autowired
    private RoleJpaRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
