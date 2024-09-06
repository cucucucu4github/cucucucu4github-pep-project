package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


/*
 * TABLE Account:  
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
    /**
     * method that return all accounts from dataset.
     * @return List<Account> that contains all accounts. May be empty.
     */
    public List<Account> getAllAccounts(){

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account;";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

            // execute the query in try-with resources, and then put all accounts in list
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    int newId = rs.getInt("account_id");
                    String newUserName = rs.getString("username");
                    String newPassword = rs.getString("password");
                    Account newAccount = new Account(newId, newUserName, newPassword);
                    accounts.add(newAccount);
                }
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * query a specific account by id
     * @param id
     * @return Account object if query success. Null if query not exist or SQL failed.
     */
    public Account getAccountById(int id){

        String sql = "SELECT * FROM Account WHERE account_id = ?;";
        
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

            ps.setInt(1, id);
            
            // execute the query, and then get the account
            try(ResultSet rs = ps.executeQuery()){    
                if (rs.next()){
                    int newId = rs.getInt("account_id");
                    String newUserName = rs.getString("username");
                    String newPassword = rs.getString("password");
                    Account newAccount = new Account(newId, newUserName, newPassword);
                    return newAccount;
                }
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * return the account queried from database by given username
     * @param username The username need to search.
     * @return Account onject identified by username. Or null if not exist.
     */
    public Account getAccountByUserName(String username){

        String sql = "SELECT * FROM Account WHERE username = ?;";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

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
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * insert a new account by Account's username and password
     * @param insertAccount Account object contains information of new Account 
     * @return updated Account if success. Null if failed.
     */
    public Account insertAccount(Account insertAccount){

        String sql = "INSERT INTO Account (username, password) VALUES (?, ?);";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){
            
            ps.setString(1, insertAccount.getUsername());
            ps.setString(2, insertAccount.getPassword());
            
            // execute the query, and then get the result;
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    int generatedId = (int) rs.getLong(1);
                    Account newAccount = new Account(generatedId, insertAccount.getUsername(), insertAccount.getPassword());
                    return newAccount;
                }
            }
    
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        // if we didn't return eariler when we hit the re.next(), then we update failure.
        return null;
    }

    /**
     * update an account password by id
     * @param account Account object that need to be update, only need to contain contain valid id and password.
     * @return boolean, true means update success, fail means update failure.
     */
    public boolean updateAccountPasswordById(Account account){
        
        String sql = "UPDATE Account SET password = ? WHERE account_id = ?;";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

            // prepare the query statement
            ps.setString(1, account.getPassword());
            ps.setInt(2, account.getAccount_id());
            
            // execute the query, and then get the result;
            // the rows updated should only be 1, since all id are unique
            int updatedRowsNum = ps.executeUpdate();
            if(updatedRowsNum == 1) return true;

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * update an account password by username
     * @param account Account object that need to be update, only need to contain contain valid username and password.
     * @return boolean, true means update success, fail means update failure.
     */
    public boolean updateAccountPasswordByUsername(Account account){
        
        String sql = "UPDATE Account SET password = ? WHERE username = ?;";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

            ps.setString(1, account.getPassword());
            ps.setString(2, account.getUsername());
            
            // execute the query, and then get the result;
            // the rows updated should only be 1, since all username are unique
            int updatedRowsNum = ps.executeUpdate();
            if(updatedRowsNum == 1) return true;

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * delete account by username
     * @param username
     * @return boolean, true if delete success. false otherwise (account not exist or SQL failed)
     */
    public boolean deleteAccountByUsername(String username){

        String sql = "DELETE FROM Account WHERE username = String";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

            ps.setString(1, username);
            
            // execute the query, and then get the result;
            // the rows updated should only be 1, since all username are unique
            int rowsDeletedNum = ps.executeUpdate();
            if(rowsDeletedNum == 1) return true;

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }

        return false;
    }

    /**
     * delete account by id
     * @param id
     * @return boolean, true if delete success. false otherwise (account not exist or SQL failed)
     */
    public boolean deleteAccountById(int id){

        String sql = "DELETE FROM Account WHERE account_id = ?";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

            ps.setInt(1, id);
            
            // execute the query, and then get the result;
            // the rows updated should only be 1, since all username are unique
            int rowsDeletedNum = ps.executeUpdate();
            if(rowsDeletedNum == 1) return true;

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }

        return false;
    }
}
