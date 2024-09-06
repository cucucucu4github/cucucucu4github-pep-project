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

    /** 
     * This service to insert a new account into database
     * First check if all user input valid.
     * Then insert the account to database.
     * If insert success, return the Account inserted.
     * If account info invalid, account already exist, insert failure, return null.
     * @param account the Account object contains new account username and password
     * @return Account that just inserted, or null if failure.
     */
    public Account createAccount(Account account){

        String newUsername = account.getUsername();
        String newPassword = account.getPassword();

        // if the account information is invalid or username already exist, return null;
        if(account == null || 
            newUsername == null || newUsername.length() < 1 || 
            newPassword == null || newPassword.length() < 4 ||
            this.accountDAO.getAccountByUserName(newUsername) != null) {
            System.err.println("Account creation failure: Account information invalid.");
            return null;
        }

        // execute account insert
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
    
    /**
     * Logic that check the login condition
     * First check if user input valid.
     * Then check if username exist.
     * Then then check if username, password matchs.
     * @param account user input account information, we only need the username and password.
     * @return Account that matched. Null if no matchs. 
     */
    public Account logInAccountMatches(Account account){
        
        // if the account passed in is null, return false.
        if(account == null 
            || account.getUsername() == null || account.getPassword() == null
            || account.getUsername().length() < 1 || account.getPassword().length() < 4) 
            return null;
        
        Account accountQueried = accountDAO.getAccountByUserName(account.getUsername());

        // if the account does not exist or the account information does not match, return false.
        if(accountQueried == null || 
            !accountQueried.getUsername().equals(account.getUsername()) ||
            !accountQueried.getPassword().equals(account.getPassword())) 
            return null; 
        
        return accountQueried;
    }


    /**
     * get all accounts
     * @return List<Account> which contains all accounts
     */
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }

}
