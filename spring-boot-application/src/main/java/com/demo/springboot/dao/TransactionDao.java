package com.demo.springboot.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.demo.springboot.model.BankTransactions;

@Repository
public interface TransactionDao extends JpaRepository<BankTransactions, Long>{
	
	 List<BankTransactions> findTransactionByAccId(@Param("accountId") Long accountId);
}
