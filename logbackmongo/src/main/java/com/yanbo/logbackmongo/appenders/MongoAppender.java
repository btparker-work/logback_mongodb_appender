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
 * A logback appender for MongoDB
 */

public class MongoAppender extends UnsynchronizedAppenderBase<LoggingEvent> {
    private MongoClient mongo;
    private MongoCollection<Document> collection;
    private String host;
    private int port;
    private String database;
    private String collectionName;

    @Override
    public void start() {
        try {
            mongo = new MongoClient(host, port);
            MongoDatabase db = mongo.getDatabase(database);
            collection = db.getCollection(collectionName);
        } catch (Exception e) {
            addStatus(new ErrorStatus("target MongoDB!", this, e));
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

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public void stop() {
        mongo.close();
        super.stop();
    }

    @Override
    protected void append(LoggingEvent event) {
        collection.insertOne(getDocument(event));
    }

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
