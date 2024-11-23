package utility;

import org.bson.Document;
import java.lang.reflect.Field;

import utility.exceptions.DocumentParsingException;

public class MongoDBDocumentParser {

    public static <T> T fromDocument(Class<T> cls, Document doc) throws DocumentParsingException {
        try {
            // Create a new instance of the target class
            T instance = cls.getDeclaredConstructor().newInstance();

            // Iterate over all fields of the class
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);  // Make the field accessible (even private)

                // Get the value from the document
                Object fieldValue = doc.get(field.getName());

                // If the field is a nested object (another class), parse it recursively
                if (fieldValue instanceof Document) {
                    // Parse nested objects by calling the factory recursively
                    field.set(instance, fromDocument(field.getType(), (Document) fieldValue));
                } else {
                    // Otherwise, set the field directly
                    field.set(instance, fieldValue);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new DocumentParsingException();
        }
    }

    // Method to convert an object back to a Document
    public static <T> Document toDocument(T object) throws DocumentParsingException {
        Document doc = new Document();
        try {
            // Iterate over all fields of the class
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);  // Make the field accessible (even private)

                // Get the field value from the object
                Object value = field.get(object);

                // If the value is a nested object, recursively convert it to a document
                if (value != null && !isPrimitive(value)) {
                    doc.put(field.getName(), toDocument(value));
                } else {
                    doc.put(field.getName(), value);
                }
            }
        } catch (Exception e) {
           throw new DocumentParsingException();
        }
        return doc;
    }

    // Helper method to check if an object is a primitive or primitive wrapper
    private static boolean isPrimitive(Object value) {
        return value instanceof String || value instanceof Integer ||
                value instanceof Double || value instanceof Boolean ||
                value instanceof Long || value instanceof Float ||
                value instanceof Character || value instanceof Byte ||
                value instanceof Short;
    }

}