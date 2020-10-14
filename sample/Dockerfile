FROM  openjdk:11.0-jre-slim

#You will need the following commented part to run the dockerfile locally only
#ENV KEYVAULT_NAME=samplekv
#ENV AZURE_CLIENT_ID=####
#ENV AZURE_CLIENT_SECRET=####
#ENV AZURE_TENANT_ID=####

ARG JAR_FILE=/target/sample-0.1.0-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

CMD ["java", "-jar", "/app.jar"]
