package utility;

import entity.User;
import org.bson.Document;
import utility.data_transfer.UserDTO;
import utility.data_transfer.UserMapper;
import utility.exceptions.DocumentParsingException;

public class MongoDBUserDocumentParser {

    public static User fromDocument(Document document) throws DocumentParsingException {
        return UserMapper.fromDTO(MongoDBDocumentParser.fromDocument(document, UserDTO.class));
    }

    public static Document toDocument(User user) throws DocumentParsingException {
        return MongoDBDocumentParser.toDocument(UserMapper.toDTO(user));
    }
}
