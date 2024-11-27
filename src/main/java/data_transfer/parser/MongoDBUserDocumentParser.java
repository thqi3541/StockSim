package data_transfer.parser;

import data_transfer.UserDTO;
import data_transfer.mapper.UserMapper;
import entity.User;
import org.bson.Document;

/**
 * Utility class for converting between MongoDB {@link Document} objects and {@link User} entities.
 *
 * <p>It uses {@link UserMapper} for converting between the {@link User} entity and {@link UserDTO}, and
 * {@link MongoDBDocumentParser} for handling the lower-level serialization and deserialization between {@link Document}
 * and the {@link UserDTO}.
 */
public class MongoDBUserDocumentParser {

    /**
     * Converts a MongoDB {@link Document} into a {@link User} entity.
     *
     * @param document the MongoDB document representing the user.
     * @return the {@link User} entity constructed from the document.
     * @throws DocumentParsingException if there is an error during the deserialization or mapping process.
     */
    public static User fromDocument(Document document) throws DocumentParsingException {
        return UserMapper.fromDTO(MongoDBDocumentParser.fromDocument(document, UserDTO.class));
    }

    /**
     * Converts a {@link User} entity into a MongoDB {@link Document}.
     *
     * @param user the {@link User} entity to be converted to a MongoDB document.
     * @return the MongoDB {@link Document} representing the user.
     * @throws DocumentParsingException if there is an error during the mapping or serialization process.
     */
    public static Document toDocument(User user) throws DocumentParsingException {
        return MongoDBDocumentParser.toDocument(UserMapper.toDTO(user));
    }
}
