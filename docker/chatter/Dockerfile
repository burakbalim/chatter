FROM chatter/ubuntu_spec

RUN mkdir chatter

COPY deploy chatter

COPY chatter-service.sh chatter

CMD ["/chatter/chatter-service.sh"]

