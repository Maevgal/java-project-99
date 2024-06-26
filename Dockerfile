FROM gradle:8-jdk21-alpine

WORKDIR /

COPY / .

RUN pwd

RUN ./gradlew --no-daemon build

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar


