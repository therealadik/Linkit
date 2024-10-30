FROM ubuntu:latest
LABEL authors="fladx"

ENTRYPOINT ["top", "-b"]