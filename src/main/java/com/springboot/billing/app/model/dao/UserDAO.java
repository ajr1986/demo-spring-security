package com.springboot.billing.app.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.billing.app.model.entity.User;

public interface UserDAO extends CrudRepository<User, Long> {

	public User findByUsername(String username);
}
