FROM ghkim3221/tollgate-standalone

ENV TOLLGATE_HOME "/opt/tollgate"

RUN mkdir -p "$TOLLGATE_HOME"
RUN mkdir -p "$TOLLGATE_HOME/endpoints"

COPY *.conf "$TOLLGATE_HOME"
COPY endpoints/*.conf "$TOLLGATE_HOME/endpoints/"

WORKDIR "$TOLLGATE_HOME"

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS "-Dconfig.file=$TOLLGATE_HOME/application.conf"
