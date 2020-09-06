package com.techlead.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.techlead.model.Users;

@Repository
public interface UserDao extends CrudRepository<Users, Integer> {
	
	Users findByUsername(String username);
	
}