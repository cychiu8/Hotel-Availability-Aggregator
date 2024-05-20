FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app

COPY . /app/

CMD ["mvn","spring-boot:run"]
# CMD ["tail", "-f", "/dev/null"]