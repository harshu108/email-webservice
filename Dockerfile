FROM openjdk:11.0.15-oraclelinux7

ENV SERVER_PORT=8080
ENV DEBUG_PORT=8000
ENV APP_HOME /app
ENV SPRING_BOOT_USER email-service
ENV SPRING_BOOT_GROUP email-service

COPY assets/entrypoint.sh $APP_HOME/entrypoint.sh
COPY target/*.jar $APP_HOME/app.jar

RUN mkdir -p $APP_HOME/logs

VOLUME /tmp

RUN groupadd -r $SPRING_BOOT_USER && useradd -r -g $SPRING_BOOT_GROUP $SPRING_BOOT_USER && \
 chmod 555 $APP_HOME/entrypoint.sh && sh -c 'touch $APP_HOME/$ARTIFACT_NAME'

EXPOSE $SERVER_PORT
EXPOSE $DEBUG_PORT

WORKDIR $APP_HOME
USER $SPRING_BOOT_USER

ENTRYPOINT ["./entrypoint.sh"]
