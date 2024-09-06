package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;

    // constructor
    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    // create a new message
    // return Message, whcih is the Message just inserted
    public Message insertMessage(Message msg){
        // I assume the logic that check the user input data has been done in controller layer,
        // forexample: check if the message context is too large, check if posted_by is exist
        return this.messageDAO.insertMessage(msg);
    }

    // get all messages
    // return List<Message>
    public List<Message> getAllMessages(){
        return this.messageDAO.getAllMessages();
    }

    // get message by message id
    // return a Message object identified by given message_id
    // return null if not exist or error happened
    public Message getMessageByMessageId(int message_id){
        return this.messageDAO.getMessageById(message_id);
    }

    // get all messages by user id
    // return List<Message>
    public List<Message> getAllMessagesByUserId(int user_id){
        return this.messageDAO.getAllMessagesByUserId(user_id);
    }

    // update the message text by given message id
    // return Message updated. return null if update failed
    public Message updateMessageTextByMessageId(int id, String message_text){
        // Should I finished the "message_id exist check" in service layer or controler layer?
        
        // if message does not exist in database
        if(this.getMessageByMessageId(id) == null) {
            return null;
        }
        
        // if update failure
        if(!this.messageDAO.updateMessageTextByMessageId(id, message_text)){
            return null;
        }

        // update success
        return this.getMessageByMessageId(id);
    }

    // delete Message by message id
    // return true if delete success affect 1 row; false otherwise
    public boolean deleteMessageById(int message_id){
        return this.messageDAO.deleteMessageById(message_id);
    }

}
