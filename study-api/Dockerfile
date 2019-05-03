FROM java:openjdk-8-jre

WORKDIR /prod/program/bootapp
ADD build/libs/study-api.jar api.jar

EXPOSE 8080
CMD java -Dspring.profiles.active=production -jar api.jar