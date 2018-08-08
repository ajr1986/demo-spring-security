package com.springboot.billing.app.model.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.billing.app.model.dao.UserDAO;
import com.springboot.billing.app.model.entity.Role;

@Service()
public class UserDetailsServiceImpl implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		com.springboot.billing.app.model.entity.User user = userDAO.findByUsername(username);

		if (user == null) {
			logger.error("Error login, user '" + username + "' does not exist");
			throw new UsernameNotFoundException("User '" + username + "' does not exist");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : user.getRoles()) {
			logger.info("Role: " + role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		if (authorities.isEmpty()) {
			logger.error("Error login, user '" + username + "' has not assigned roles");
			throw new UsernameNotFoundException("User '" + username + "' has not assigned roles");
		}

		User springSecurityUser = new User(username, user.getPassword(), user.getEnabled(), true, true, true,
				authorities);

		return springSecurityUser;
	}

}
