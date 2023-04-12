FROM eclipse-temurin:17-jre

EXPOSE 8080

COPY backend/build/libs/stranger-map-backend-*.jar /usr/app/stranger-map-backend.jar

ENTRYPOINT java $JAVA_OPTS -jar /usr/app/stranger-map-backend.jar