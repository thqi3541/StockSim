package utility;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.concurrent.TimeUnit;


public class MongoDBClientManager {

    private static final long DATABASE_CONNECTION_TIMEOUT = 10;

    private static final MongoClient instance = createInstance();

    static {
        // Close MongoClient at shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(MongoDBClientManager::closeMongoClient));
    }

    private static MongoClient createInstance() {
        Dotenv dotenv = Dotenv.configure().filename(".env.local").load();
        String connectionString = dotenv.get("MONGODB_API_KEY");
        ServerApi serverApi = ServerApi.builder()
                                       .version(ServerApiVersion.V1)
                                       .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                                                          .applyConnectionString(new ConnectionString(connectionString))
                                                          .applyToClusterSettings(builder ->
                                                                                          builder.serverSelectionTimeout(
                                                                                                  DATABASE_CONNECTION_TIMEOUT,
                                                                                                  TimeUnit.SECONDS))
                                                          .serverApi(serverApi)
                                                          .build();
        return MongoClients.create(settings);
    }

    public static MongoClient Instance() {
        return instance;
    }

    // Close the MongoClient when done
    public static void closeMongoClient() {
        if (instance != null) {
            instance.close();
        }
    }
}