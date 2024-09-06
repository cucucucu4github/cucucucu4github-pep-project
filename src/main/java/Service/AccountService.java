package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;

public class AccountService {
    
    private AccountDAO accountDAO;

    // constructor, create a DAO for this object
    public AccountService(){
        this.accountDAO = new AccountDAO();
    }

    // create a new account
    // return account object created. 
    // return null if account alreadyexist or database insert failed.
    public Account createAccount(Account account){

        // if the account passed in is null, return null;
        if(account == null) {
            System.err.println("Account creation failure: Account you passed in is empty.");
            return null;
        }

        // check if the account username already exist, return null if so.
        if(accountDAO.getAccountByUserName(account.getUsername()) != null){
            System.err.println("Account creation failure: Account you want to create already exist: " + account.getUsername());
            return null;
        }

        Account newAccount = accountDAO.insertAccount(account);

        // check if account insert failed, return null if so.
        if(newAccount == null){
            System.err.println("Account creation failure: Account insert failure: " + account.getUsername());
            return null;
        }

        return newAccount;
    }

    /**
     * Get the Account by given username
     * @param username the username given by user
     * @return account the account queried by username
     */
    public Account getAccountByUsername(String username){
        return this.accountDAO.getAccountByUserName(username);
    }
    

    // user account log in service
    // return true if account exist and password matchs username.
    // return false otherwise.
    public boolean logIn(Account account){
        
        // if the account passed in is null, return false.
        if(account == null || account.getUsername() == null || account.getPassword() == null) 
            return false;
        
        Account accountQueried = accountDAO.getAccountByUserName(account.getUsername());

        // if the account does not exist or the account information does not match, return false.
        if(accountQueried == null || 
            !accountQueried.getUsername().equals(account.getUsername()) ||
            !accountQueried.getPassword().equals(account.getPassword())) 
            return false; 
        
        return true;
    }
    
    // get all accounts
    // this method may not use, but I included it here.
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

}
