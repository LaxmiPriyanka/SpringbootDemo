package com.demo.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.demo.springboot.model.BankUser;

@Repository
public interface UserDao extends JpaRepository<BankUser, Long>{
	
	public BankUser findByName(@Param("username") String username);
	
}


