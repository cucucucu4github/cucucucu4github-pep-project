package Service;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    // constructor
    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }


    /**
     * First check if all user inputs are valid: posted_by exist, message not empty and not over 255 char.
     * Then insert message to database.
     * @param message should contain all information except the message_id.
     * @return Message, whcih is the Message just inserted. Null if userinput invalid or isnert failed.
     */
    public Message insertMessage(Message message){
        
        // check if userinput valid
        if(message == null || 
            message.getMessage_text() == null || 
            message.getMessage_text().length() < 1 ||
            message.getMessage_text().length() > 255){
           return null; 
        }

        // check if posted_by exist in Account table
        if(this.accountDAO.getAccountById(message.getPosted_by()) == null){
            return null;
        }
        
        return this.messageDAO.insertMessage(message);
    }

    /**
     * get all messages
     * @return List<Message>
     */
    public List<Message> getAllMessages(){
        return this.messageDAO.getAllMessages();
    }

    /**
     * get message by message id
     * @param message_id
     * @return quired message, which could be null if message_id not exist or SQL failed.
     */
    public Message getMessageByMessageId(int message_id){
        return this.messageDAO.getMessageById(message_id);
    }

    /**
     * get all messages by user id
     * @param account_id
     * @return List<Message>
     */
    public List<Message> getAllMessagesByUserId(int account_id){
        return this.messageDAO.getAllMessagesByUserId(account_id);
    }

    // update the message text by given message id
    // return Message updated. return null if update failed
    /**
     * update the message by given message id.
     * then return the message just been updated.
     * since the user input only include message id and message text, 
     * I have to use get... method to query the updated message for return.
     * @param message_id message id
     * @param message_text new message text
     * @return Message just been updated
     */
    public Message updateMessageTextByMessageId(int message_id, String message_text){
        // Should I finished the "message_id exist check" in service layer or controler layer?
        
        // if message does not exist in database
        if(this.getMessageByMessageId(message_id) == null) {
            return null;
        }
        
        // if update failure
        if(!this.messageDAO.updateMessageTextByMessageId(message_id, message_text)){
            return null;
        }

        // update success
        return this.getMessageByMessageId(message_id);
    }

    /**
     * Check if message exist.
     * If exist, delete it, and retuen the Message been deleted.
     * Otherwise, retun null
     * @param message_id 
     * @return Message that been deleted. Null if not exist or SQL failed.
     */
    public Message deleteMessageById(int message_id){
        
        // query the message to check if it exist
        Message deletedMessage = this.messageDAO.getMessageById(message_id);
        
        // directly return if not exist.
        if(deletedMessage == null) return null;
        
        // delete the message
        this.messageDAO.deleteMessageById(message_id);

        return deletedMessage;
    }

}
