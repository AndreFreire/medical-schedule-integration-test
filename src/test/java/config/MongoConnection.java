package config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnection {
    static MongoClient mongoClient = new MongoClient("localhost",62979);
    static MongoDatabase dataBase = mongoClient.getDatabase("test");
    static MongoCollection<Document> scheduleCollection = dataBase.getCollection("schedule");
    static MongoCollection<Document> appointmentCollection = dataBase.getCollection("appointment");

    public static MongoCollection<Document> getScheduleCollection() {
        return scheduleCollection;
    }

    public static MongoCollection<Document> getAppointmentCollection() {
        return appointmentCollection;
    }
}
