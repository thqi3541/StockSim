package utility;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class MongoDBClientManager {

    private static MongoClient instance;

    public static MongoClient Instance() {
        if (instance == null) {
            // Note: for security concern, MongoDB access credential is stored locally in environment variable
            // To create new client with credential, invoke the following commands:
            // Windows:
            // setx MONGODB_URI "mongodb+srv://username:password@cluster.mongodb.net/mydb"
            // Linux:
            // export MONGODB_URI="mongodb+srv://username:password@cluster.mongodb.net/mydb"
            // source ~/.bashrc
            String connectionString = System.getenv("MONGODB_URI");
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .serverApi(serverApi)
                    .build();
            instance = MongoClients.create(settings);
        }
        return instance;
    }

    // Close the MongoClient when done
    public static void closeMongoClient() {
        if (instance != null) {
            instance.close();
        }
    }
}