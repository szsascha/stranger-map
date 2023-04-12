FROM eclipse-temurin:17-jre

EXPOSE 8080

COPY build/libs/stranger-map-backend-*.jar /usr/app/

ENTRYPOINT java $JAVA_OPTS -jar /usr/app/stranger-map-backend.jar