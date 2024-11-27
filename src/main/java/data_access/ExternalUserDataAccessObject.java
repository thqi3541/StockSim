package data_access;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import data_transfer.parser.DocumentParsingException;
import data_transfer.parser.MongoDBUserDocumentParser;
import entity.User;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.login.LoginDataAccessInterface;
import use_case.registration.RegistrationDataAccessInterface;
import use_case.view_history.ViewHistoryDataAccessInterface;
import utility.MongoDBManager;
import utility.ServiceManager;
import utility.SessionManager;
import utility.exceptions.ValidationException;

import java.rmi.ServerException;

import static com.mongodb.client.model.Filters.eq;


public class ExternalUserDataAccessObject implements
                                          RegistrationDataAccessInterface,
                                          LoginDataAccessInterface,
                                          ExecuteBuyDataAccessInterface,
                                          ViewHistoryDataAccessInterface {

    public ExternalUserDataAccessObject() {
        ServiceManager.Instance()
                      .registerService(UserDataAccessInterface.class,
                                       this);
        ServiceManager.Instance()
                      .registerService(RegistrationDataAccessInterface.class,
                                       this);
        ServiceManager.Instance()
                      .registerService(LoginDataAccessInterface.class, this);

        ServiceManager.Instance()
                      .registerService(ExecuteBuyDataAccessInterface.class,
                                       this);
        ServiceManager.Instance()
                      .registerService(ViewHistoryDataAccessInterface.class,
                                       this);
    }

    @NotNull
    private static MongoCollection<Document> getUserCollection() {
        MongoDatabase database =
                MongoDBManager.Instance().getDatabase("StockSimDB");
        return database.getCollection("users");
    }

    @Override
    public User getUserWithCredential(String credential)
            throws ValidationException {
        // get username from credential
        String username = SessionManager.Instance().getUsername(credential)
                                        .orElseThrow(ValidationException::new);
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
    public void updateUserData(User user) throws ServerException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            collection.replaceOne(
                    eq("username", user.getUsername()),
                    MongoDBUserDocumentParser.toDocument(user)
            );
        } catch (DocumentParsingException e) {
            throw new ServerException("Parsing document error", e);
        }
    }

    @Override
    public User getUserWithPassword(String username, String password)
            throws ValidationException {
        // retrieve user data from database
        try {
            return getUserByQuery(
                    new Document("username", username).append("password",
                                                              password));
        } catch (DocumentParsingException e) {
            // TODO: use a more robust logging approach than printStackTrace
            e.printStackTrace();
            throw new ValidationException();
        }
    }

    private User getUserByQuery(Document query)
            throws ValidationException, DocumentParsingException {
        MongoCollection<Document> collection = getUserCollection();
        Document result = collection.find(query).first();
        if (result == null) {
            throw new ValidationException();
        }
        System.out.println(result);
        return MongoDBUserDocumentParser.fromDocument(result);
    }

    @Override
    public boolean hasUsername(String username) {
        MongoCollection<Document> collection = getUserCollection();
        Document query = new Document("username", username);

        return collection.countDocuments(query) > 0;
    }

    @Override
    public void createUser(User user) throws DocumentParsingException {
        MongoCollection<Document> collection = getUserCollection();
        Document userDocument = MongoDBUserDocumentParser.toDocument(user);
        userDocument.remove("_id");
        collection.insertOne(userDocument);
    }
}
