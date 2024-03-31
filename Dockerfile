FROM gradle:8-jdk21-alpine

WORKDIR /app

COPY /app .

RUN pwd

RUN ./gradlew --no-daemon build

CMD ./build/install/app/bin/app
