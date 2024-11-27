package utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import utility.exceptions.DocumentParsingException;

/**
 * Utility class for parsing objects to and from BSON Documents.
 */
public class MongoDBDocumentParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parses a BSON Document into an instance of the specified class.
     *
     * @param document the BSON Document to parse
     * @param clazz    the class to map the Document to
     * @param <T>      the type parameter of the class
     * @return the parsed object
     * @throws DocumentParsingException if parsing fails
     */
    public static <T> T fromDocument(Document document, Class<T> clazz)
            throws DocumentParsingException {
        try {
            document.remove("_id");
            String json = document.toJson();
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocumentParsingException(
                    "Failed to parse document to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Converts an object into a BSON Document.
     *
     * @param object the object to convert
     * @return the BSON Document representation of the object
     * @throws DocumentParsingException if conversion fails
     */
    public static Document toDocument(Object object)
            throws DocumentParsingException {
        try {
            return Document.parse(objectMapper.writeValueAsString(object));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocumentParsingException(
                    "Failed to convert object to BSON Document", e);
        }
    }
}
