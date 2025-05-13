FROM openjdk:21.0.2-jdk-slim

COPY ./out/artifacts/ProgettoScuoleItaliane_jar/ProgettoScuoleItalian.jar /app/ProgettoScuoleItalian.jar
COPY ./Anagrafe-delle-scuole-italiane.csv /app/Anagrafe-delle-scuole-italiane.csv

WORKDIR /app

EXPOSE 1717
EXPOSE 1771

CMD ["java", "-jar", "ProgettoScuoleItalian.jar"]

#docker build .

#copiare l'identificativo
#docker run -p 1717:1717 -p 1771:1771 <identificativo>