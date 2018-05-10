package com.yanbo.logbackmongo.appenders;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.ErrorStatus;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * A customized logback MongoDB appender
 *
 * @author Yanbo
 */
public class MongoAppender extends UnsynchronizedAppenderBase<LoggingEvent> {
    private MongoClient mongoClient;
    private MongoCollection<Document> collection;
    private String host;
    private int port;
    private String databaseName;
    private String collectionName;

    @Override
    public void start() {
        try {
            mongoClient = new MongoClient(host, port);
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            collection = db.getCollection(collectionName);
        } catch (Exception e) {
            addStatus(new ErrorStatus("Failed to connect to the targeted mongoDB. Please specify the right properties's names.", this, e));
            return;
        }
        super.start();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
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
        doc.append("timestamp", new Date(event.getTimeStamp()));
        doc.append("level", event.getLevel().toString());
        doc.append("logger", event.getLoggerName());
        doc.append("thread", event.getThreadName());
        doc.append("message", event.getFormattedMessage());
        return doc;
    }

}
