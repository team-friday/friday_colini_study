FROM java:openjdk-8-jre

WORKDIR /prod/program/bootapp
ADD build/libs/study-messaging.jar messaging.jar

EXPOSE 8081
CMD java -Dspring.profiles.active=production -Dio.netty.leakDetectionLevel=advanced -jar messaging.jar