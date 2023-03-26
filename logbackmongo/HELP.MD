POC: 
This POC proves...

Steps to run POC:
1) setup free mongo atlas cluster that you have access to

2) using mongosh, run the commands in scripts/create_test_data.txt to create an example database

3) build the microservice

4) open 3 cmd windows and run each instance with it's own instance name, like this command which creates 'instanceA' of the service:
mvn spring-boot:run -Dmaven.test.skip=true -Dspring-boot.run.jvmArguments="-Djdk.tls.client.protocols=TLSv1.2" -Dspring-boot.run.arguments="--instance.name=instanceA --server.port=8080"

5) Look in the database at the GroceryOrder collection and notice all orders are moved from 'open' to 'wip'.  Also look at the GroceryOrderLog collection and observe that no single order was updated by any more than one instance of the microservice and that the different records were processed by different instances of the service.

-----

mongodb://<username>:<password>@cluster0-shard-00-00.itynz.mongodb.net:27017,cluster0-shard-00-01.itynz.mongodb.net:27017,cluster0-shard-00-02.itynz.mongodb.net:27017/?ssl=true&replicaSet=atlas-pgmbzp-shard-0&authSource=admin&retryWrites=true&w=majority

mvn clean install -Dmaven.test.skip=true -Dspring-boot.run.jvmArguments="-Djdk.tls.client.protocols=TLSv1.2" -Dspring-boot.run.arguments="--instance.name=instanceZ --server.port=8083"

mvn spring-boot:run -Dmaven.test.skip=true -Dspring-boot.run.jvmArguments="-Djdk.tls.client.protocols=TLSv1.2" -Dspring-boot.run.arguments="--instance.name=instanceA --server.port=8080"

mvn spring-boot:run -Dmaven.test.skip=true -Dspring-boot.run.jvmArguments="-Djdk.tls.client.protocols=TLSv1.2" -Dspring-boot.run.arguments="--instance.name=instanceB --server.port=8081"

mvn spring-boot:run -Dmaven.test.skip=true -Dspring-boot.run.jvmArguments="-Djdk.tls.client.protocols=TLSv1.2" -Dspring-boot.run.arguments="--instance.name=instanceC --server.port=8082"