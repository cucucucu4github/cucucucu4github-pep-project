package Controller;

import Service.AccountService;
import Service.MessageService;

import Model.Account;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
     * First check if username exist
     * Then then check if username, password matchs.
     * If matches, response the inserted account as json and set 200.
     * Otherwise, set 401.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void loginHandler(Context context){

    }

    /**
     * Handler for create new Message
     * First check if all user inputs are valid: posted_by exist, message not empty and not over 255 char.
     * Then insert message to database.
     * If success, return message, set 200.
     * Otherwise, set 400.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createMessageHandler(Context context){

    }

    /**
     * Handler for retrive all messages
     * Response all messages get from database as json.
     * Always set 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context){

    }

    /**
     * Handler to get Message by message id
     * Response Message as json if message exist. 
     * Otherwise empty.
     * Always set 200.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIdHandler(Context context){

    }

    /**
     * Handler to delete the message by message id
     * Check if message exist.
     * If exist, delete it, response with deleted message as hson, and set 200.
     * If not exist, response with empty, set 200.
     * @param context
     */
    private void deleteMessageByIdHandler(Context context){

    }

    /**
     * Handler to update message by message id
     * First check if message exist.
     * Then check message text valid, not empty and not over 255 char.
     * Response Message if success, set 200.
     * Otherwise set 400.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void updateMessageHandler(Context context){

    }

    /**
     * Handler to get all messages fron a given user.
     * First check if account id exist.
     * Then query all messages from the user.
     * Response all messages if success, set 200.
     * Otherwise set 400.
     *  @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesFromAccountIdHandler(Context context){

    }
}