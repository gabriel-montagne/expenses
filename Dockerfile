FROM clojure
MAINTAINER Gabriel Munteanu <gabimunteanu.sdt@gmail.com>

RUN mkdir -p /code
WORKDIR /code
ADD . /code
RUN lein deps
