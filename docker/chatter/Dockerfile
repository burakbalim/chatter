FROM chatter/ubuntu_spec

RUN mkdir chatter

COPY deploy chatter

ENTRYPOINT java -jar /chatter/chatter.messaging-1.0-SNAPSHOT.jar --configFile=/chatter/chatter.json
