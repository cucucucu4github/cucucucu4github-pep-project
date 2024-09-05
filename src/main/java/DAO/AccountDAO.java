package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


/*
 * table Account:  
 * account_id   integer         primary key auto_increment,
 * username     varchar(255)    unique,
 * password     varchar(255)
*/

public class AccountDAO {
    /* 
     * Due to the mini project and the readme, 
     * it seems like I better get connection in each method, instead of create a static one.
     * But why?
     * public static Connection con = ConnectionUtil.getConnection();
    */

    // return all accounts
    public List<Account> getAllAccounts(){

        List<Account> accounts = new ArrayList<>();

        try(Connection connection = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM Account;";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int newId = rs.getInt("account_id");
                String newUserName = rs.getString("username");
                String newPassword = rs.getString("password");
                Account newAccount = new Account(newId, newUserName, newPassword);
                accounts.add(newAccount);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    // return a specific account by id
    public Account getAccountById(int id){
        return null;
    }

    // insert a new account, return boolean
    // true: update successful
    // false: update failure
    public Boolean insertAccount(Account account){
        return true;
    }

    // update an account, return boolean.
    // true: update successful
    // false: update failure
    public Boolean updateAccount(Account account){
        return true;
    }

    // delete account by id, return boolean
    // true: delete successful
    // false: delete failure
    public Boolean deleteAccountById(int id){
        return true;
    }

}
