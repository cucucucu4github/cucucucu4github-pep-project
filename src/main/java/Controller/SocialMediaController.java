package Controller;

import Service.AccountService;
import Service.MessageService;

import Model.Account;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    // init the Controler
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("register", this::registerNewUserHandler);
        app.post("login", this::loginHandler);
        
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesFromAccountIdHandler);

        return app;
    }


    /**
     * This handler for register a new user.
     * If success, response the inserted account as json and set 200.
     * Otherwise, set 400.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerNewUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account newAccount = mapper.readValue(context.body(), Account.class);
        
        // create the new account
        newAccount = this.accountService.createAccount(newAccount);
        
        // check if insert failed
        if(newAccount == null){
            context.status(400);
            return;
        }

        // insert success
        context.status(200);
        context.json(newAccount);
    }

    /**
     * Handler for user login
     * Call service layer to check the login matches.
     * If matches, response the matched account as json and set 200.
     * Otherwise, set 401.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        // call service layer to check the login condition
        Account matchedAccount = this.accountService.logInAccountMatches(account);

        // if no match
        if(matchedAccount == null) {
            context.status(401);
            return;
        }

        // if matches
        context.status(200);
        context.json(matchedAccount);
    }

    /**
     * Handler for create new Message
     * If success, return message, set 200.
     * Otherwise, set 400.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     *      The body should only contains posted_by, message_text and time.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createMessageHandler(Context context) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        // call service layer to create a new message and insert
        Message newMessage = this.messageService.insertMessage(message);

        if(newMessage == null){
            context.status(400);
            return;
        }

        context.status(200);
        context.json(newMessage);
    }

    /**
     * Handler for retrive all messages
     * Response all messages get from database as json.
     * Always set 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        List<Message> allMessages = this.messageService.getAllMessages();
        context.status(200);
        context.json(allMessages);
    }

    /**
     * Handler to get Message by message id
     * Response Message as json if message exist. 
     * Otherwise empty.
     * Always set 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessageByIdHandler(Context context) throws JsonProcessingException {
        
        // get message id
        int message_id = Integer.valueOf(context.pathParam("message_id"));
        // qyery the message
        Message quiredMessage = this.messageService.getMessageByMessageId(message_id);

        if(quiredMessage == null){
            // message_id not exist or SQL failed
            context.status(200);
            return;
        }
        context.status(200);
        context.json(quiredMessage); // what if quiredMessage == null?
    }

    /**
     * Handler to delete the message by message id
     * If delete success, response with deleted message as hson, and set 200.
     * If delete failed, may because not exist or SQL failed, response with empty, set 200.
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException {
        
        int deletedMessageId = Integer.valueOf(context.pathParam("message_id"));
        Message deletedMessage = this.messageService.deleteMessageById(deletedMessageId);

        if(deletedMessage == null){
            context.status(200);
            return;
        }
        context.status(200).json(deletedMessage);
    }

    /**
     * Handler to update message by message id
     * First check if message exist.
     * Then check message text valid, not empty and not over 255 char.
     * Response Message if success, set 200.
     * Otherwise set 400.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        
        // get message id and text
        int message_id = Integer.valueOf(context.pathParam("message_id"));
        String message_text = message.getMessage_text();

        Message updatedMessage = this.messageService.updateMessageTextByMessageId(message_id, message_text);

        // if update failed
        if(updatedMessage == null){
            context.status(400);
            return;
        }
        context.status(200);
        context.json(updatedMessage);
    }

    /**
     * Handler to get all messages fron a given user.
     * First check if account id exist.
     * Then query all messages from the user.
     * Response all messages if success, set 200.
     * Otherwise set 400.
     *  @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesFromAccountIdHandler(Context context) throws JsonProcessingException {

        // get account id
        int account_id = Integer.valueOf(context.pathParam("account_id"));

        List<Message> userMessages = this.messageService.getAllMessagesByUserId(account_id);
        
        context.status(200);
        context.json(userMessages);
    }
}