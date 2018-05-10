# logback_mongodb_appender


This is an Logback appender for writing logs to the Mongo database.


### Basic steps of the appender

- Extend the `ch.qos.logback.core.UnsynchronizedAppenderBase` class
- connect to Mongo instance
- Fetch logs from `LoggingEvent`
- write logs to Mongo document

### Usage

Download the Maven project and directly run it.

The MongoDB properties's names are specified in `application.properties`. 
If you don't use Spring, in the logback-spring.xml, you can remove all `springProperty` tags and hard code your `host`, `port` ,and etc in the appender's list.

