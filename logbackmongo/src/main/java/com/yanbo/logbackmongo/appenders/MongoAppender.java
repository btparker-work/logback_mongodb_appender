package com.yanbo.logbackmongo.appenders;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.ErrorStatus;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * A customized logback MongoDB appender
 *
 * @author Yanbo
 */
public class MongoAppender extends UnsynchronizedAppenderBase<LoggingEvent> {
    private MongoClient mongoClient;
    private MongoCollection<Document> collection;
    private String databaseName;
    private String collectionName;
    private String connectionUri;

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoCollection<Document> getCollection() {
        return this.collection;
    }

    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public String getConnectionUri() {
        return this.connectionUri;
    }

    public void setConnectionUri(String connectionUri) {
        this.connectionUri = connectionUri;
    }

    @Override
    public void start() {
        try {
            ConnectionString connectionString = new ConnectionString(connectionUri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();  
            MongoClient mongoClient = MongoClients.create(settings);
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            collection = db.getCollection(collectionName);
        } catch (Exception e) {
            addStatus(new ErrorStatus(
                    "Failed to connect to the targeted mongoDB. Please specify the right properties's names.", this,
                    e));
            return;
        }
        super.start();
    }

    public void setdatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public void stop() {
        mongoClient.close();
        super.stop();
    }

    @Override
    protected void append(LoggingEvent event) {
        collection.insertOne(getDocument(event));
    }

    // customize the document
    private Document getDocument(LoggingEvent event) {
        Document doc = new Document();
        // append the fields fetched from the event
        doc.append("service", "JobScheduler");
        doc.append("instance", "JobScheduler_WSEDSS1232");
        doc.append("timestamp", new Date(event.getTimeStamp()));
        doc.append("level", event.getLevel().toString());
        doc.append("logger", event.getLoggerName());
        doc.append("thread", event.getThreadName());
        doc.append("message", event.getFormattedMessage());
        return doc;
    }

}
