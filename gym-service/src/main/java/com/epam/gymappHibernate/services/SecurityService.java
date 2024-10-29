package com.epam.gymappHibernate.services;

import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.entity.SecurityUser;
import com.epam.gymappHibernate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;

@Service
public class SecurityService implements UserDetailsService {


    private UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Set<GrantedAuthority> authorities = getAuthoritiesForUser(user);

        return new SecurityUser(user, authorities);
    }

    private Set<GrantedAuthority> getAuthoritiesForUser(User user) {

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));


        return authorities;
    }
}