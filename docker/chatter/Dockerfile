FROM chatter/ubuntu_spec

ARG CHATTER_HOME="/opt/chatter"

COPY deploy ${CHATTER_HOME}

COPY chatter-service.sh ${CHATTER_HOME}

WORKDIR ${CHATTER_HOME}

CMD ["./chatter-service.sh", "start"]

