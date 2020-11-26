FROM ghkim3221/tollgate-standalone

ENV TOLLGATE_HOME "/opt/tollgate"

RUN mkdir -p "$TOLLGATE_HOME"
RUN mkdir -p "$TOLLGATE_HOME/routing"

COPY *.conf "$TOLLGATE_HOME"
COPY routing "$TOLLGATE_HOME/routing/"

WORKDIR "$TOLLGATE_HOME"

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS "-Dconfig.file=$TOLLGATE_HOME/application.conf"
