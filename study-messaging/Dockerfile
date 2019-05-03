FROM java:openjdk-8-jre

WORKDIR /prod/program/bootapp
ADD build/libs/study-messaging.jar messaging.jar

EXPOSE 8080
CMD java -Dspring.profiles.active=production -jar messaging.jar