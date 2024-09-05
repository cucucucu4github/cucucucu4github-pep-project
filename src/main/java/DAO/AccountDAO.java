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

            // prepare the query statement
            String sql = "SELECT * FROM Account;";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            // execute the query, and then put all accounts in list
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
        
        try(Connection connection = ConnectionUtil.getConnection()){

            // prepare the query statement
            String sql = "SELECT * FROM Account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            // execute the query, and then get the account
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                int newId = rs.getInt("account_id");
                String newUserName = rs.getString("username");
                String newPassword = rs.getString("password");
                Account newAccount = new Account(newId, newUserName, newPassword);
                return newAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // return a specific account by username
    // the reason I need this method is I do not know the id when user want to login/register
    public Account getAccountByUserName(String username){
        
        try(Connection connection = ConnectionUtil.getConnection()){

            // prepare the query statement
            String sql = "SELECT * FROM Account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            
            // execute the query, and then get the account
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                int newId = rs.getInt("account_id");
                String newUserName = rs.getString("username");
                String newPassword = rs.getString("password");
                Account newAccount = new Account(newId, newUserName, newPassword);
                return newAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // insert a new account by username and password, return updated Account
    // return null if failure
    public Account insertAccount(String newUsername, String newPassword){

        try(Connection connection = ConnectionUtil.getConnection()){

            // prepare the query statement
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newUsername);
            ps.setString(2, newPassword);
            
            // execute the query, and then get the result;
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int generatedId = (int) rs.getLong(1);
                Account newAccount = new Account(generatedId, newUsername, newPassword);
                return newAccount;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        // if we didn't return eariler when we hit the re.next(), then we update failure.
        return null;
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
