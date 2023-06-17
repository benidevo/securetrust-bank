FROM docker.elastic.co/elasticsearch/elasticsearch:7.17.10

ENV ES_JAVA_OPTS="-Xms512m -Xmx512m"

COPY elasticsearch.yml /usr/share/elasticsearch/config/

EXPOSE 9200

CMD ["elasticsearch"]
