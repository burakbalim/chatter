FROM openjdk:8-jdk

RUN mkdir -p /app/chatter

ARG CHATTER_HOME="/app/chatter"

RUN git clone https://github.com/hburakbalim/chatter.git \
  && cd chatter \
  && ./gradlew build -x test \
  && mv ./chatter.messaging/build/libs/chatter.messaging-*.jar /app/chatter/ \
  && cd / \
  && rm -rf chatter

COPY /config ${CHATTER_HOME}

COPY chatter-service.sh ${CHATTER_HOME}

WORKDIR ${CHATTER_HOME}

CMD ["/app/chatter/chatter-service.sh", "start"]
