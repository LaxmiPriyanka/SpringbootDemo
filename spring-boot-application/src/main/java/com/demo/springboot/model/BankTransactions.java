package com.demo.springboot.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.demo.springboot.util.model.AbstractModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
	@NamedQuery(name = "BankTransactions.findTransactionByAccId", query = "SELECT u FROM BankTransactions u WHERE u.account.id =:accountId ")
/*@Entity
@Table(name = "employee", schema="spring_data_jpa_example")
@NamedQuery(name = "Employee.fetchByLastNameLength",
        query = "SELECT e FROM Employee e WHERE CHAR_LENGTH(e.lastname) =:length "
)*/
@SQLDelete(sql="UPDATE BankTransactions SET deleted = '1' WHERE id = ?")
@Where(clause="deleted=0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankTransactions extends AbstractModel<Long> implements Serializable{
	/**
	 * //It holds bank account transaction details
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	
	private Date date;
	private String description;
	private double amount;
	private double balance;
	
	@ManyToOne
	@JoinColumn(name="accountId")
	private BankAccount account;
	
	public BankTransactions()
	{
		
	}
	
	public BankTransactions(String description,double amount,Date date,BankAccount account)
	{
		this.account=account;
		this.description=description;
		this.date=date;
		this.amount=amount;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public BankAccount getBankAccount() {
		return account;
	}
	public void setBankAccount(BankAccount account) {
		this.account = account;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}