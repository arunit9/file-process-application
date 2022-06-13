package com.app.fileprocess.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.fileprocess.dao.entity.User;
import com.app.fileprocess.dao.repository.UserJpaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
    private UserJpaRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);

        return UserDetailsImpl.build(user);
    }
}
