package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/*
 * TABLE Message
 * 
 * message_id           integer         primary key auto_increment,
 * posted_by            integer,
 * message_text         varchar(255),
 * time_posted_epoch    long,
 * 
 * foreign key (posted_by) references Account(account_id)
 */

public class MessageDAO {

    // insert a new message
    // return Message inserted
    public Message insertMessage(int posted_by, String message_text, long time_posted){
        
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);){

                ps.setInt(1, posted_by);
                ps.setString(2, message_text);
                ps.setLong(3, time_posted);

                // execute update, get the result set
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys();){
                    if(rs.next()){
                        int keyGenerated = rs.getInt(1);
                        Message newMessage = new Message(keyGenerated, posted_by, message_text, time_posted);
                        return newMessage;
                    }
                }
                
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return null;
    }

    // get all message
    // return list of messages
    public List<Message> getAllMessages(){

        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message;";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {

                // execute the query and get the resultset
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        int message_id = rs.getInt("message_id");
                        int posted_by = rs.getInt("posted_by");
                        String message_text = rs.getString("message_text");
                        long time_posted = rs.getLong("time_posted_epoch");
                        messages.add(new Message(message_id, posted_by, message_text, time_posted));
                    }
                }

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }

        return messages;
    }

    // get all message by user id (posted_by)
    // return list of Messages
    public List<Message> getAllMessagesByUserId(int user_id){
        
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?;";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {

                ps.setInt(1, user_id);

                // execute the query and get the resultset
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        int message_id = rs.getInt("message_id");
                        int posted_by = rs.getInt("posted_by");
                        String message_text = rs.getString("message_text");
                        long time_posted = rs.getLong("time_posted_epoch");
                        messages.add(new Message(message_id, posted_by, message_text, time_posted));
                    }
                }

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }

        return messages;
    }

    // get message by message id
    // return Messages
    public Message getMessageById(int message_id){

        String sql = "SELECT * FROM Message WHERE message_id = ?;";

        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);) {

                ps.setInt(1, message_id);

                // execute the query and get the resultset
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        Message message = new Message(rs.getInt("message_id"), 
                            rs.getInt("posted_by"), 
                            rs.getString("message_text"), 
                            rs.getLong("time_posted_epoch"));
                        return message;
                    }
                }

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        // if nothing found, return null;
        return null;
    }

    // update message_text by message id
    // return boolean
    public boolean updateMessageTextByMessageId(int message_id, String message_text){
        
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
        
        try(Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);){

                ps.setString(1, message_text);
                ps.setInt(2, message_id);

                // execute update, get the result set
                int numRowsUpdated = ps.executeUpdate();
                return numRowsUpdated == 1;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    // delete message by id
    // return boolean, true means delete successful, false means delete failure
    public boolean deleteMessageById(int message_id){
        
        String sql = "DELETE FROM Message WHERE message_id = ?;";

        try(Connection connection = ConnectionUtil.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);){

            ps.setInt(1, message_id);

            // execute update, get the result set
            int numRowsUpdated = ps.executeUpdate();
            return numRowsUpdated == 1;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;

    }

    
}
