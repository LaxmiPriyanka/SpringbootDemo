package com.demo.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demo.springboot.model.BankAccount;

@Repository
public interface AccountDao extends JpaRepository<BankAccount, Long>  {


}