FROM adoptopenjdk/openjdk17:alpine-jre
ENV ARTIFACT_NAME=stranger-map-backend-1.0.0.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}