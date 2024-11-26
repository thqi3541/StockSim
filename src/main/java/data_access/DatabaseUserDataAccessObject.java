package data_access;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.login.LoginDataAccessInterface;
import use_case.view_history.ViewHistoryDataAccessInterface;
import utility.MongoDBClientManager;
import utility.MongoDBDocumentParser;
import utility.ServiceManager;
import utility.SessionManager;
import utility.exceptions.DocumentParsingException;
import utility.exceptions.ValidationException;


public class DatabaseUserDataAccessObject implements
        LoginDataAccessInterface,
        ExecuteBuyDataAccessInterface,
        ViewHistoryDataAccessInterface
{

    public DatabaseUserDataAccessObject() {
        ServiceManager.Instance().registerService(DatabaseUserDataAccessObject.class, this);
        ServiceManager.Instance().registerService(LoginDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ExecuteBuyDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ViewHistoryDataAccessInterface.class, this);
    }


    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        // get username from credential
        String username = SessionManager.Instance().getUsername(credential).orElseThrow(ValidationException::new);
        // retrieve user data from database
        try {
            return getUserByQuery(new Document("username", username));
        } catch (DocumentParsingException e) {
            // TODO: use a more robust logging approach than printStackTrace
            e.printStackTrace();
            throw new ValidationException();
        }
    }

    @Override
    public User getUserWithPassword(String username, String password) throws ValidationException {
        // retrieve user data from database
        try {
            return getUserByQuery(new Document("username", username).append("password", password));
        } catch (DocumentParsingException e) {
            // TODO: use a more robust logging approach than printStackTrace
            e.printStackTrace();
            throw new ValidationException();
        }
    }

    private User getUserByQuery(Document query) throws ValidationException, DocumentParsingException {
        MongoDatabase database = MongoDBClientManager.Instance().getDatabase("StockSimDB");
        MongoCollection<Document> collection = database.getCollection("users");
        Document result = collection.find(query).first();
        if (result == null) {
            throw new ValidationException();
        }
        return MongoDBDocumentParser.fromDocument(result, User.class);
    }
}
