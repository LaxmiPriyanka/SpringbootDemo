package com.demo.springboot.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.demo.springboot.util.model.AbstractModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity

@SQLDelete(sql="UPDATE BankAccount SET deleted = '1' WHERE id = ?")
@Where(clause="deleted=0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccount extends AbstractModel<Long> implements Serializable  {

	
	@Transient
	private static final long serialVersionUID = 1L;
	
	private String accName;
	private String accType;
	private double minBal;
	private double balance;
	
	public static final String SAVINGS="savingsAccount";
	public static final String CURRENT="currentAccount";
	
	public static final double MIN_BAL_OF_SAVINGS_ACCOUNT=500;
	public static final double MIN_BAL_OF_CURRENT_ACCOUNT=1000;

	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private BankUser user;
	
	 @OneToMany(mappedBy="account",cascade=CascadeType.ALL)
	 private Collection<BankTransactions> transactions;
	
	public BankAccount(){}
	
	public BankAccount(String accName,String accType,String userName,String password)
	{
		this.accName=accName;
    	this.accType=accType;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}
	
	public double getMinBal() {
		return minBal;
	}

	public void setMinBal(double minBal) {
		this.minBal = minBal;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public static String getSavings() {
		return SAVINGS;
	}

	public static String getCurrent() {
		return CURRENT;
	}

	public static double getMinBalOfSavingsAccount() {
		return MIN_BAL_OF_SAVINGS_ACCOUNT;
	}

	public static double getMinBalOfCurrentAccount() {
		return MIN_BAL_OF_CURRENT_ACCOUNT;
	}

	public BankUser getUser() {
		return user;
	}

	public void setBankUser(BankUser user) {
		this.user = user;		
	}

	public Collection<BankTransactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(Collection<BankTransactions> transactions) {
		this.transactions = transactions;
	}
}
