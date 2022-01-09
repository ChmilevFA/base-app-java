FROM openjdk:17
ADD build/libs/*.jar app.jar
EXPOSE 7000
ENTRYPOINT ["java","-jar","app.jar"]