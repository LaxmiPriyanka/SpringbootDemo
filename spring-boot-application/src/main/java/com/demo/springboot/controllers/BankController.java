package  com.demo.springboot.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.springboot.dao.AccountDao;
import com.demo.springboot.dao.TransactionDao;
import com.demo.springboot.dao.UserDao;
import com.demo.springboot.model.BankAccount;
import com.demo.springboot.model.BankTransactions;
import com.demo.springboot.model.BankUser;
//import com.demo.springboot.service.BankServices;
import com.demo.springboot.util.JSONUtil;
import com.demo.springboot.util.JsonWrapper;


@RestController
public class BankController {

/*	@Autowired
	private BankServices bankService;*/
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private AccountDao accountDao;
	
	//validating username and password
	@RequestMapping(value="login")
	public String userLoginValidator(@RequestBody String requestJson) {
		
		//to get the username and password from json
		String username = (String)JSONUtil.parseJSON(requestJson, "username", "String");
		String password = (String)JSONUtil.parseJSON(requestJson, "password", "String");
		
		System.out.println("user name is :: " + username);
		System.out.println("password is :: " + password);
		
		//for validation of user to login
		BankUser bankUser =validateUser(username,password);
		String result = "{}";
		if(bankUser == null) {
			result = "{\"result\":\"failed\"}";
		}
		else {
			result = "{\"result\":\"success\",\"userType\":\""+bankUser.getUserType()+"\",\"userId\":\""+bankUser.getId()+"\"}";
		}
		
		return result;
	}
	public BankUser validateUser(String username, String password) {
		System.out.println("In validate service");
		BankUser user = userDao.findByName(username);
		System.out.println(user);
		if(user != null) {
			if(!(user.getPassword().equals(password))) {
				return null;
			}
		}
		System.out.println(user);
		return user;
	}
	//For getting all accounts 
	@RequestMapping(value="admin")
	public @ResponseBody String getAllAccounts() throws Exception{
		//to get all list of bank accounts 
		List<BankAccount> accounts = accountDao.findAll();
		JsonWrapper wrapper=new JsonWrapper();		
		if(accounts.size()>0){
			
			wrapper.writeStartArray();
			for(BankAccount account:accounts)
			{
				wrapper.writeStartObject();
				wrapper.writeStringField("accountNumber", String.valueOf(account.getId()));
				wrapper.writeStringField("accountHolderName",account.getAccName());
				wrapper.writeStringField("accountType", account.getAccType());
				wrapper.writeEndObject();
			}
			wrapper.writeEndArray();
		}
		else
		{
			wrapper.writeStartObject();
			wrapper.writeStringField("result", "no accounts");
			wrapper.writeEndObject();
		}
		wrapper.close();
		return wrapper.getJsonValue();
	}
	//creating accounts
	@RequestMapping(value="new", method=RequestMethod.POST)
	public @ResponseBody String createAccount(@RequestBody String requestJson) throws Exception{
		
		String accountHolderName= (String)JSONUtil.parseJSON(requestJson, "accountHolderName", "String");
		String accountType= (String)JSONUtil.parseJSON(requestJson, "accountType", "String");
		String username = (String)JSONUtil.parseJSON(requestJson, "username", "String");
		String password = (String)JSONUtil.parseJSON(requestJson, "password", "String");
		
		//boolean res=bankService.createAccount(accountHolderName, accountType, username, password);
		
		BankUser user= userDao.findByName(username);
		if(user!=null){
			return "{\"result\":\"failed\"}";
		}
		else{

			BankAccount bankAccount = new BankAccount();
			bankAccount.setAccName(accountHolderName);
			bankAccount.setAccType(accountType);
			if(accountType.equals(BankAccount.SAVINGS))
			{
				bankAccount.setMinBal(BankAccount.MIN_BAL_OF_SAVINGS_ACCOUNT);					
				bankAccount.setBalance(BankAccount.MIN_BAL_OF_SAVINGS_ACCOUNT);
			}
			else if(accountType.equals(BankAccount.CURRENT))	
			{
				bankAccount.setMinBal(BankAccount.MIN_BAL_OF_CURRENT_ACCOUNT);
				bankAccount.setBalance(BankAccount.MIN_BAL_OF_CURRENT_ACCOUNT);
			}
			accountDao.save(bankAccount);
			BankUser bankUser= new BankUser();
			bankUser.setUserName(username);
			bankUser.setPassword(password);
			bankUser.setUserType("C");
			
			bankUser.setAccount(bankAccount);
			userDao.save(bankUser);
			
			BankTransactions bankTransactions = new BankTransactions();
			bankTransactions.setDescription("created");
			bankTransactions.setAmount(bankAccount.getBalance());
			bankTransactions.setDate(new Date());
			bankTransactions.setBalance(bankAccount.getBalance());
			
			bankTransactions.setBankAccount(bankAccount);
			transactionDao.save(bankTransactions);
		
			return "{\"result\":\"success\"}";
		
		}
		
		/*System.out.println(res);
		String result="{}";
		if(res==true){
			return "{\"result\":\"success\"}";
		}
		else
		{
			result = "{\"result\":\"failed\"}";
		}
		return result;*/
						
	}
	//get all the accounts
	@RequestMapping(value="new/{id}",method=RequestMethod.GET)
    public @ResponseBody String toGetAccountById(@PathVariable(value="id")Long Id) throws Exception{
		Optional<BankAccount> bankAccount=accountDao.findById(Id);
		BankAccount account = bankAccount.get();
		BankUser bankUser=bankAccount.get().getUser();
        JsonWrapper wrapper= new JsonWrapper();
                wrapper.writeStartObject();              
                wrapper.writeStringField("accountHolderName", account.getAccName());
                wrapper.writeStringField("accountType", account.getAccType());
                wrapper.writeStringField("username", bankUser.getUserName());
                wrapper.writeStringField("password", bankUser.getPassword());
                wrapper.writeEndObject();
       
        wrapper.close();
        return wrapper.getJsonValue();
    }
	
	//Updating account
	@RequestMapping(value="new/{id}",method=RequestMethod.PUT)
    public @ResponseBody String updateAccountById(@PathVariable("id")Long Id,@RequestBody String requestJson) throws Exception{
		long id=(Long)JSONUtil.parseJSON(requestJson, "id", "long");
		String accountHolderName = (String)JSONUtil.parseJSON(requestJson, "accountHolderName", "String");
		//String accountType = (String)JSONUtil.parseJSON(requestJson, "accountType", "String");
		String username = (String)JSONUtil.parseJSON(requestJson, "username", "String");
		String password = (String)JSONUtil.parseJSON(requestJson, "password", "String");
		
		//boolean res = bankService.updateAccountById(id,accountHolderName,username,password);
		
		
		Optional<BankAccount> bankAccount=accountDao.findById(id);
		BankAccount ba = bankAccount.get();
		ba.setAccName(accountHolderName);
		BankUser bankUser=bankAccount.get().getUser();
		bankUser.setUserName(username);
		bankUser.setPassword(password);
		accountDao.save(ba);
		userDao.save(bankUser);
		 return "{\"result\":\"success\"}";
		
		/*System.out.println(res);
		String result="{}";		
		if(res==true)
		{
			result = "{\"result\":\"success\"}";
		}
		else
		{
			result = "{\"result\":\"failed\"}";
		}*/
		/*return result;	*/
    }
	//Deleting Account 
	@RequestMapping(value="new/{id}",method= RequestMethod.DELETE)
	public @ResponseBody String deleteAccountById(@PathVariable("id")long Id) {
		System.out.println("In Bank controller Delete");
		boolean res= deleteAccount(Id);
		System.out.println(res);
		String result="{}";
		if(res==true)
		{
			result = "{\"result\":\"success\"}";
		}
		else
		{
			result = "{\"result\":\"failed\"}";
		}
		return result;
	}
	
	public boolean deleteAccount(long id) {
		Optional<BankAccount> bankAccount=accountDao.findById(id);
		BankUser bankUser=bankAccount.get().getUser();
		Collection<BankTransactions> bankTransaction=bankAccount.get().getTransactions();		
		
		for(BankTransactions transaction:bankTransaction){
			transactionDao.delete(transaction);
		}
		
		userDao.delete(bankUser);
		accountDao.delete(bankAccount.get());
		return true;
	}

	@RequestMapping(value="accountHolder/{id}",method=RequestMethod.PUT)
    public @ResponseBody String setTransactions(@PathVariable(value="id")Long Id,@RequestBody String requestJson) throws Exception{
		Optional<BankUser> bankUser=userDao.findById(Id);
		if(bankUser.get()!=null) {
			BankAccount bankAccount=bankUser.get().getBankAccount();
			String desc=(String)JSONUtil.parseJSON(requestJson,"desc","String");
			double amount =(Double) JSONUtil.parseJSON(requestJson,"amount","double");
			Long accNumber=bankAccount.getId();
			String res="{}";
			boolean result=false;
	        double balance = 0;
	        if(desc.equals("deposit")){ 
				result = depositAmount(accNumber,amount,desc);
			}
			else if(desc.equals("withdraw")){ 
					result = withdrawAmount(accNumber,amount,desc);
				}
				Optional<BankUser> bu = userDao.findById(Id);
			  	balance = bu.get().getBankAccount().getBalance();
				if(result==true)
				{
					res = "{\"result\":\"success\", \"balance\":\""+balance+"\"}";
				}
				else if(result==false)
				{
					res = "{\"result\":\"failed\"}";
				}
			
			return res;  
		}else {
			return "{\"result\":\"User details not found\"}";
		}
		
		
	}
	
	@RequestMapping(value="accountTrasactions/{id}",method=RequestMethod.GET)
	public @ResponseBody String getAllTransactions(@PathVariable(value="id")Long Id) throws Exception{
		Optional<BankUser> bankUser=userDao.findById(Id);
		if(bankUser.get()!=null) {
		BankAccount bankAccount = bankUser.get().getBankAccount();
		long accNumber=bankAccount.getId();
		List<BankTransactions> transactions = transactionDao.findTransactionByAccId(accNumber);
		JsonWrapper wrapper=new JsonWrapper();
		if(transactions.size()>0){
			wrapper.writeStartArray();
			for(BankTransactions transaction:transactions){
				wrapper.writeStartObject();
				wrapper.writeStringField("date",transaction.getDate().toString());
				wrapper.writeStringField("description",transaction.getDescription());
				wrapper.writeStringField("amount",String.valueOf(transaction.getAmount()));
				wrapper.writeStringField("balance",String.valueOf(transaction.getBalance()));
				wrapper.writeEndObject();
			}
			wrapper.writeEndArray();			
		}else{
			wrapper.writeStartObject();
			wrapper.writeStringField("result", "no accounts");
			wrapper.writeEndObject();
		}
		wrapper.close();
		System.out.println(wrapper.getJsonValue());
		return wrapper.getJsonValue();
		}else {
			return "{\"result\":\"User details not found\"}";
		}
	}
	
	public boolean depositAmount(Long accNumber, double amount, String description) {
		Optional<BankAccount> bankAccount=accountDao.findById(accNumber);
		double balance=bankAccount.get().getBalance();
		balance=amount+balance;
		bankAccount.get().setBalance(balance);
		accountDao.save(bankAccount.get());
		
		BankTransactions bankTransaction=new BankTransactions();
		bankTransaction.setAmount(amount);
		Date date=  new Date();
		bankTransaction.setDate(date);
		bankTransaction.setDescription(description);
		bankTransaction.setBalance(balance);
		bankTransaction.setBankAccount(bankAccount.get());
		transactionDao.save(bankTransaction);
		return true;
	}
	public boolean withdrawAmount(long accNumber, double amount,
			String description) {
		Optional<BankAccount> bankAccount=accountDao.findById(accNumber);
		double balance=bankAccount.get().getBalance();
		double minBal=bankAccount.get().getMinBal();
		if(balance>amount && (balance-amount)>=minBal)
		{
			balance=balance-amount;
			bankAccount.get().setBalance(balance);
			accountDao.save(bankAccount.get());
			
			BankTransactions bankTransaction=new BankTransactions();
			bankTransaction.setAmount(amount);
			Date date=  new Date(System.currentTimeMillis());
			bankTransaction.setDate(date);
			bankTransaction.setDescription(description);
			bankTransaction.setBankAccount(bankAccount.get());
			bankTransaction.setBalance(balance);
			transactionDao.save(bankTransaction);
			return true;
		}
		else
		{
			return false;
		}
	}
	/*@RequestMapping(value = "logout", method = RequestMethod.POST)
	public @ResponseBody String logout (HttpServletRequest request, HttpServletResponse response) {
		try {
			//TODO
			return "{\"success\":\"yes\"}";
		}
		catch(Exception e){}
		return "{\"success\":\"no\"}";
	}*/
	/*@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}*/
	/*@RequestMapping(value = "logout", method = RequestMethod.POST)
	public @ResponseBody String logout (HttpServletRequest request, HttpServletResponse response) {
		try {
			SecurityContextHolder.clearContext();
			return "{\"success\":\"yes\"}";
		}
		catch(Exception e){}
		return "{\"success\":\"no\"}";
	}*/
}
