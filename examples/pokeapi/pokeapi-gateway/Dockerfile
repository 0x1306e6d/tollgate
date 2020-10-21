FROM ghkim3221/tollgate-standalone

ENV TOLLGATE_HOME "/opt/tollgate"

RUN mkdir -p "$TOLLGATE_HOME/conf"

COPY application.conf "$TOLLGATE_HOME/conf"

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS "-Dconfig.file=$TOLLGATE_HOME/conf/application.conf"
