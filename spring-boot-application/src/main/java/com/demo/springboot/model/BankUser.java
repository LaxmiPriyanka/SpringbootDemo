package com.demo.springboot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.demo.springboot.util.model.AbstractModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "BankUser")
	@NamedQuery(name="BankUser.findByName", query="select u from BankUser u where u.username like :username")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankUser extends AbstractModel<Long> implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	
	private String username;
    private String password;
    private String userType;
    
    @OneToOne
	@JoinColumn(name="accountId")
	private BankAccount account;
    
    public BankUser()
    {
    	
    }
    public BankUser(String username, String password,BankAccount account){
		this.username = username;
	    this.password = password;       
	    this.account=account;
	}  
    
    public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public BankAccount getBankAccount() {
		return account;
	}
	public void setAccount(BankAccount account) {
		this.account = account;
	} 
}
