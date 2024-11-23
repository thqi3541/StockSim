package data_access;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.view_history.ViewHistoryDataAccessInterface;
import utility.MongoDBClientManager;
import utility.SessionManager;
import utility.exceptions.ValidationException;


public class UserDatabase implements
        ExecuteBuyDataAccessInterface,
        ViewHistoryDataAccessInterface
{


    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        // get username from credential
        String username = SessionManager.Instance().getUsername(credential).orElseThrow(ValidationException::new);
        // retrieve user data from database
        try {
            MongoDatabase database = MongoDBClientManager.Instance().getDatabase("StockSimDB");
            MongoCollection<Document> collection = database.getCollection("users");
            Document query = new Document("username", username);
            Document result = collection.find(query).first();
            if (result == null) {
                throw new ValidationException();
            }
            return
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
