package utility;

import org.bson.Document;
import utility.exceptions.DocumentParsingException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ReflectionMongoDBDocumentParser {

    /**
     * Parses a MongoDB {@link Document} into an instance of the specified class.
     * <p>
     * The method uses reflection to map the fields in the {@link Document} to the fields of the
     * specified class. It supports nested objects by recursively parsing fields that are represented
     * as {@link Document} in MongoDB. The constructor matching the fields of the class is used to
     * create the instance.
     * </p>
     *
     * @param cls The {@link Class} of the object to be parsed.
     * @param doc The MongoDB {@link Document} to be parsed.
     * @param <T> The type of the object being created.
     * @return An instance of the specified class populated with data from the {@link Document}.
     * @throws DocumentParsingException If the parsing process encounters errors, such as missing
     *                                  fields or invalid field types.
     */
    public static <T> T fromDocument(Document doc, Class<T> cls)
            throws DocumentParsingException {
        // drop _id field of document
        doc.remove("_id");
        try {
            // Find the constructor with parameters matching the document's fields
            Constructor<?> matchingConstructor = null;
            for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
                Parameter[] parameters = constructor.getParameters();

                // Check if parameter names match document keys
                if (doesConstructorMatch(parameters, doc)) {
                    matchingConstructor = constructor;
                    break;
                }
            }

            if (matchingConstructor == null) {
                throw new RuntimeException();
            }

            // Prepare arguments for the constructor
            Object[] constructorArgs =
                    Arrays.stream(matchingConstructor.getParameters())
                          .map(param -> parseParameter(param, doc))
                          .toArray();

            // Create an instance using the matching constructor
            matchingConstructor.setAccessible(true);
            return cls.cast(matchingConstructor.newInstance(constructorArgs));

        } catch (Exception e) {
            e.printStackTrace();
            throw new DocumentParsingException(
                    "Error parsing document to " + cls.getName(), e);
        }
    }

    // Check if the document matches any constructor parameters
    private static boolean doesConstructorMatch(Parameter[] parameters,
                                                Document doc) {
        // Collect parameter names
        Set<String> paramNames = Arrays.stream(parameters)
                                       .map(Parameter::getName)
                                       .collect(Collectors.toSet());

        // Collect document field names
        Set<String> docFieldNames = doc.keySet();

        // Check if all parameter name set and document set are equal
        return paramNames.containsAll(docFieldNames) &&
                docFieldNames.containsAll(paramNames);
    }

    private static Object parseParameter(Parameter param, Document doc) {
        Object value = doc.get(param.getName());

        try {
            // Handle nested objects by parsing them recursively
            if (value instanceof Document) {
                return fromDocument((Document) value, param.getType());
            }
            // Handle collections with nested entities
            if (value instanceof Iterable<?> iterable) {
                // Check the generic type of the collection
                if (param.getParameterizedType() instanceof ParameterizedType parameterizedType) {
                    Class<?> genericType =
                            (Class<?>) parameterizedType.getActualTypeArguments()[0];

                    // Recursively parse elements if they are entities
                    if (genericType.getPackageName().startsWith("entity")) {
                        return StreamSupport.stream(iterable.spliterator(),
                                                    false)
                                            .map(item -> {
                                                try {
                                                    return item instanceof Document ?
                                                            fromDocument(
                                                                    (Document) item,
                                                                    genericType) :
                                                            item;
                                                } catch (
                                                        DocumentParsingException e) {
                                                    throw new RuntimeException(
                                                            e);
                                                }
                                            })
                                            .collect(
                                                    Collectors.toCollection(
                                                            () -> createEmptyCollection(
                                                                    value)));
                    }
                }
            }

        } catch (DocumentParsingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        // Handle basic types directly
        return value;
    }


    private static Collection<Object> createEmptyCollection(Object iterable) {
        return switch (iterable) {
            case List list -> new ArrayList<>();
            case Set set -> new HashSet<>();
            case Queue queue -> new LinkedList<>();
            case null, default -> throw new UnsupportedOperationException(
                    "Unsupported collection type: " +
                            iterable.getClass().getName());
        };
    }


    /**
     * Converts an object into a MongoDB {@link Document}.
     * <p>
     * The method uses reflection to iterate through all fields of the provided object. Fields that
     * are nested objects are recursively converted into nested {@link Document}. Primitive fields and
     * supported types (e.g., {@link String}, {@link Integer}, {@link Date}) are added to the
     * {@link Document} directly.
     * </p>
     *
     * @param object The object to be converted into a MongoDB {@link Document}.
     * @param <T>    The type of the object being converted.
     * @return A {@link Document} representing the provided object's data.
     * @throws DocumentParsingException If the conversion process encounters errors, such as
     *                                  inaccessible fields or unsupported types.
     */
    public static <T> Document toDocument(T object)
            throws DocumentParsingException {
        Document doc = new Document();
        doc.remove("_id");
        try {
            // Iterate over all fields of the class
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);

                // If the value is a nested object, recursively convert it to a document
                if (isDocument(value)) {
                    doc.put(field.getName(), toDocument(value));
                } else {
                    doc.put(field.getName(), value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocumentParsingException(
                    "Failed to parse object into document ", e);
        }
        return doc;
    }

    // Helper method to check if an object is another entity
    // The object is an entity if its class is in entity directory
    private static boolean isDocument(Object value) {
        if (value == null) {
            return false;
        }
        Package objectPackage = value.getClass().getPackage();
        return objectPackage != null &&
                objectPackage.getName().startsWith("entity");
    }

}